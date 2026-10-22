package com.lorenzon;

public class Cell {

    private Player player;

    public boolean isEmpty() {
        return player == null;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean placeMark(Player player) {
        if (player == null || !isEmpty()) {
            return false;
        }

        this.player = player;
        return true;
    }

    public void clear() {
        player = null;
    }
}
