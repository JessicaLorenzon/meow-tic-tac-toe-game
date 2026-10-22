package com.lorenzon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameRenderer {

    private Game game;

    Texture cat1Texture;
    Texture cat2Texture;
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    FitViewport viewport;
    BitmapFont font;
    GlyphLayout layout;

    float cellSize = 1.6f;
    float gridSize = 3 * cellSize;
    float startX;
    float startY;

    public GameRenderer(Game game) {
        this.game = game;

        cat1Texture = new Texture("cat1.png");
        cat2Texture = new Texture("cat2.png");

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        viewport = new FitViewport(10, 7);

        font = new BitmapFont();
        font.getData().setScale(0.035f);
        font.setUseIntegerPositions(false);

        layout = new GlyphLayout();
    }

    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
    }

    public void render() {
        drawBackground();
        drawBoard();
        drawCells();
        drawMessage();
    }

    private void drawBackground() {
        Color softGreen = new Color(216f / 255f, 226f / 255f, 220f / 255f, 1.0f);

        ScreenUtils.clear(softGreen);
        viewport.apply();
    }

    public void drawBoard() {
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(Color.BLACK);

        float worldWidth = viewport.getWorldWidth();

        startX = (worldWidth - gridSize) / 2f;
        startY = 0.1f;

        // Vertical lines
        for (int i = 1; i < 3; i++) {
            float x = startX + i * cellSize;
            shapeRenderer.line(x, startY, x, startY + gridSize);
        }

        // Horizontal lines
        for (int i = 1; i < 3; i++) {
            float y = startY + i * cellSize;
            shapeRenderer.line(startX, y, startX + gridSize, y);
        }

        shapeRenderer.end();
    }

    public void drawCells() {
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Cell cell = game.getBoard().getCell(row, col);

                if (!cell.isEmpty()) {
                    float x = (col * cellSize) + startX;
                    float y = (row * cellSize) + startY;

                    if (cell.getPlayer() == Player.PLAYER_1) {
                        batch.draw(cat1Texture, x, y, cellSize, cellSize);
                    } else {
                        batch.draw(cat2Texture, x, y, cellSize, cellSize);
                    }
                }
            }
        }

        batch.end();
    }

    public void drawMessage() {
        String message;

        if (game.getStatus() == GameStatus.PLAYER_WON) {
            message = "Player " + game.getWinner() + " winner!\nPress the R key to restart";
        } else if (game.getStatus() == GameStatus.DRAW) {
            message = "Draw!\nPress the R key to restart";
        } else {
            message = "Player " + game.getCurrentPlayer() + " turn";
        }

        layout.setText(font, message);

        float textX = (viewport.getWorldWidth() - layout.width) / 2f;
        float textY = viewport.getWorldHeight() - 0.3f;

        batch.begin();

        font.setColor(Color.BLACK);
        font.draw(batch, layout, textX, textY);

        batch.end();
    }

    public boolean isInsideGrid(Vector2 touch) {
        unproject(touch);

        return touch.x >= startX && touch.x < startX + gridSize && touch.y >= startY && touch.y < startY + gridSize;
    }

    public void unproject(Vector2 touch) {
        viewport.unproject(touch);
    }

    public int getRow(Vector2 touch) {
        return (int) ((touch.y - startY) / cellSize);
    }

    public int getCol(Vector2 touch) {
        return (int) ((touch.x - startX) / cellSize);
    }

    public void dispose() {
        cat1Texture.dispose();
        cat2Texture.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
