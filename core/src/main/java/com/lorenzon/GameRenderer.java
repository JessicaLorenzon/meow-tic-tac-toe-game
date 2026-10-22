package com.lorenzon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameRenderer {

    private Game game;

    Music backgroundMusic;
    Sound placeSound;
    Sound gameOverSound;
    Texture cat1Texture;
    Texture cat2Texture;
    Texture pawsTexture;
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

        cat1Texture = new Texture("images/cat1.png");
        cat2Texture = new Texture("images/cat2.png");
        pawsTexture = new Texture("images/paws.png");

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        viewport = new FitViewport(10, 7);

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/background.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.20f);
        backgroundMusic.play();
        placeSound = Gdx.audio.newSound(Gdx.files.internal("audio/place.mp3"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("audio/game-over.mp3"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Nunito-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 64;
        parameter.color = Color.valueOf("748579");
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        font = generator.generateFont(parameter);
        font.getData().setScale(0.01f);
        font.setUseIntegerPositions(false);
        generator.dispose();

        layout = new GlyphLayout();
    }

    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
    }

    public void render() {
        drawBackground();
        drawPaws();
        drawBoard();
        drawCells();
        drawMessage();
    }

    private void drawBackground() {
        ScreenUtils.clear(Color.valueOf("E5EBE5"));
        viewport.apply();
    }

    private void drawPaws() {
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        batch.setColor(1f, 1f, 1f, 0.16f);
        batch.draw(pawsTexture, 0f, 0f, 10f, 7f);

        batch.setColor(Color.WHITE);

        batch.end();
    }

    public void drawBoard() {
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        startX = (viewport.getWorldWidth() - gridSize) / 2f;
        startY = 0.1f;

        float lineThickness = 0.055f;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.valueOf("748579"));

        // Vertical lines
        for (int i = 1; i < 3; i++) {
            float x = startX + i * cellSize;

            shapeRenderer.rect(x - lineThickness / 2f, startY, lineThickness, gridSize);
        }

        // Horizontal lines
        for (int i = 1; i < 3; i++) {
            float y = startY + i * cellSize;

            shapeRenderer.rect(startX, y - lineThickness / 2f, gridSize, lineThickness);
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

        font.setColor(Color.valueOf("748579"));

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.setColor(Color.WHITE);
        batch.begin();

        String[] lines = message.split("\n");

        float textY = viewport.getWorldHeight() - 0.3f;

        for (String line : lines) {
            layout.setText(font, line);

            float textX = (viewport.getWorldWidth() - layout.width) / 2f;

            font.draw(batch, line, textX, textY);

            textY -= layout.height + 0.1f;
        }

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

    public void playPlaceSound() {
        placeSound.play(0.5f);
    }

    public void playGameOverSound() {
        gameOverSound.play(0.7f);
    }

    public void dispose() {
        cat1Texture.dispose();
        cat2Texture.dispose();
        pawsTexture.dispose();

        backgroundMusic.dispose();
        placeSound.dispose();
        gameOverSound.dispose();

        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
