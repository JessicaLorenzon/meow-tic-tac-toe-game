package com.lorenzon;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main implements ApplicationListener {

    ShapeRenderer shapeRenderer;
    FitViewport viewport;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        viewport = new FitViewport(8, 5);
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        draw();
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float cellSize = 1.6f;
        float gridSize = 3 * cellSize;

        float startX = (worldWidth - gridSize) / 2f;
        float startY = (worldHeight - gridSize) / 2f;

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
        // Destroy application's resources here.
    }
}
