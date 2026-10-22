package com.lorenzon;

public class Game {

    private Board board;
    private Player currentPlayer;
    private Player winner;
    private GameStatus status;

    public Game() {
        board = new Board();
        currentPlayer = Player.PLAYER_1;
        status = GameStatus.IN_PROGRESS;
    }

    public boolean play(int row, int col) {
        if (status != GameStatus.IN_PROGRESS) {
            return false;
        }
        if (!board.placeMark(row, col, currentPlayer)) {
            return false;
        }

        winner = checkWinner(row, col);

        if (winner != null) {
            status = GameStatus.PLAYER_WON;
        } else if (board.isFull()) {
            status = GameStatus.DRAW;
        } else {
            changePlayer();
        }

        return true;
    }

    public Player checkWinner(int row, int col) {
        Player player = board.getCell(row, col).getPlayer();

        if (
            // check row
            board.isCellsEqual(board.getCell(row, 0), board.getCell(row, 1), board.getCell(row, 2)) ||
                // check column
                board.isCellsEqual(board.getCell(0, col), board.getCell(1, col), board.getCell(2, col)) ||
                // check diagonal
                board.isCellsEqual(board.getCell(0, 0), board.getCell(1, 1), board.getCell(2, 2)) ||
                // check diagonal
                board.isCellsEqual(board.getCell(0, 2), board.getCell(1, 1), board.getCell(2, 0))
        ) {
            return player;
        }
        return null;
    }

    public void restart() {
        board.reset();

        currentPlayer = Player.PLAYER_1;
        status = GameStatus.IN_PROGRESS;
    }

    public Board getBoard() {
        return board;
    }

    public GameStatus getStatus() {
        return status;
    }

    public String getCurrentPlayer() {
        return currentPlayer == Player.PLAYER_1 ? "1" : "2";
    }

    public String getWinner() {
        return winner == Player.PLAYER_1 ? "1" : "2";
    }

    private void changePlayer() {
        currentPlayer = currentPlayer == Player.PLAYER_1 ? Player.PLAYER_2 : Player.PLAYER_1;
    }

}
