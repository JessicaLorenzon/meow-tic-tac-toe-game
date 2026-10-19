package com.lorenzon;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main implements ApplicationListener {

    Texture cat1Texture;
    Texture cat2Texture;
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    FitViewport viewport;
    Vector2 touch;

    float cellSize = 1.6f;
    float gridSize = 3 * cellSize;
    float startX;
    float startY;
    int[][] cats;
    int turn;
    int moves;
    boolean winner;
    boolean gameOver;

    @Override
    public void create() {
        cat1Texture = new Texture("cat1.png");
        cat2Texture = new Texture("cat2.png");
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        viewport = new FitViewport(8, 5);
        touch = new Vector2();
        cats = new int[3][3];
        turn = 1;
        moves = 0;
        winner = false;
        gameOver = false;
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() {
        if (Gdx.input.justTouched()) {
            if (gameOver) return;
            float touchX = Gdx.input.getX();
            float touchY = Gdx.input.getY();

            viewport.unproject(touch.set(touchX, touchY));

            float mouseX = touch.x;
            float mouseY = touch.y;

            boolean insideGrid = mouseX >= startX && mouseX < startX + gridSize && mouseY >= startY && mouseY < startY + gridSize;

            if (insideGrid) {
                int column = (int) ((mouseX - startX) / cellSize);
                int line = (int) ((mouseY - startY) / cellSize);

                if (cats[line][column] != 0) return;

                cats[line][column] = turn;
                moves++;
                turn = (turn == 1) ? 2 : 1;
            }
        }
    }

    private void logic() {
        if (gameOver) return;

        for (int line = 0; line < 3; line++) {
            if (cats[line][0] != 0 && cats[line][0] == cats[line][1] && cats[line][1] == cats[line][2]) {
                System.out.println("Victory line!");
                winner = true;
                break;
            }
        }
        for (int column = 0; column < 3 && !winner; column++) {
            if (cats[0][column] != 0 && cats[0][column] == cats[1][column] && cats[1][column] == cats[2][column]) {
                System.out.println("Victory column!");
                winner = true;
            }
        }

        if (!winner && cats[0][0] != 0 && cats[0][0] == cats[1][1] && cats[1][1] == cats[2][2]) {
            System.out.println("Victory diagonal 1!");
            winner = true;
        }

        if (!winner && cats[0][2] != 0 && cats[0][2] == cats[1][1] && cats[1][1] == cats[2][0]) {
            System.out.println("Victory diagonal 2!");
            winner = true;
        }

        if (winner) {
            gameOver = true;
        } else if (moves == 9) {
            System.out.println("Draw!");
            gameOver = true;
        }
    }

    private void draw() {
        Color softGreen = new Color(216f / 255f, 226f / 255f, 220f / 255f, 1.0f);

        ScreenUtils.clear(softGreen);
        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        startX = (worldWidth - gridSize) / 2f;
        startY = (worldHeight - gridSize) / 2f;

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

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        for (int line = 0; line < 3; line++) {
            for (int column = 0; column < 3; column++) {
                if (cats[line][column] != 0) {
                    float x = (column * cellSize) + startX;
                    float y = (line * cellSize) + startY;

                    if (cats[line][column] == 1) {
                        batch.draw(cat1Texture, x, y, cellSize, cellSize);
                    } else {
                        batch.draw(cat2Texture, x, y, cellSize, cellSize);
                    }
                }
            }
        }

        batch.end();
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void dispose() {
        cat1Texture.dispose();
        batch.dispose();
        shapeRenderer.dispose();
    }
}
