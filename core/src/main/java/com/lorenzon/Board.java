package com.lorenzon;

public class Board {

    private static final int SIZE = 3;

    private final Cell[][] cells = new Cell[SIZE][SIZE];

    public Board() {
        initializeCells();
    }

    private void initializeCells() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col] = new Cell();
            }
        }
    }

    public Cell getCell(int row, int col) {
        if (!isValidPosition(row, col)) {
            throw new IndexOutOfBoundsException(
                "Invalid position: " + row + ", " + col
            );
        }
        return cells[row][col];
    }

    public boolean placeMark(int row, int col, Player player) {
        if (!isValidPosition(row, col)) {
            return false;
        }

        return cells[row][col].placeMark(player);
    }

    public boolean isFull() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (cells[row][col].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void reset() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col].clear();
            }
        }
    }

    public boolean isCellsEqual(Cell first, Cell second, Cell third) {
        return !first.isEmpty() && first.getPlayer() == second.getPlayer() && second.getPlayer() == third.getPlayer();
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }
}
