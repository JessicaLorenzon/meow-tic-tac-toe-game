package com.lorenzon;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class Main implements ApplicationListener {

    private Game game;
    private GameRenderer renderer;

    @Override
    public void create() {
        game = new Game();
        renderer = new GameRenderer(game);
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void render() {
        input();
        renderer.render();
    }

    private void input() {
        if (Gdx.input.justTouched() && game.getStatus() == GameStatus.IN_PROGRESS) {
            Vector2 touch = new Vector2(Gdx.input.getX(), Gdx.input.getY());

            if (renderer.isInsideGrid(touch)) {
                int row = renderer.getRow(touch);
                int col = renderer.getCol(touch);

                Cell cell = game.getBoard().getCell(row, col);

                if (cell.isEmpty()) {
                    game.play(row, col);

                    renderer.playPlaceSound();

                    if (game.getStatus() == GameStatus.PLAYER_WON || game.getStatus() == GameStatus.DRAW) {
                        renderer.playGameOverSound();
                    }
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            game.restart();
        }
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
        renderer.dispose();
    }
}
