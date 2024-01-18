package org.example.server;

import java.util.regex.Pattern;

public class Game implements Runnable {
    private static final int BOARD_SIZE = 4;
    private final ClientHandler player1;
    private final ClientHandler player2;
    private ClientHandler currentPlayerTurn;
    private boolean gameOver = false;

    public Game(ClientHandler player1, ClientHandler player2) {
        this.player1 = player1;
        this.player2 = player2;
        currentPlayerTurn = player1;
    }


    @Override
    public void run() {
        playGame();
    }

    private void playGame() {
        while (!gameOver) {
            sendBoards(currentPlayerTurn);
            currentPlayerTurn.sendMessage("Your turn (e.g.: A3): ");
            getOpponent(currentPlayerTurn).sendMessage("Waiting for opponent move ...");
            processPlayerMove(currentPlayerTurn);
            checkWinner();
            switchCurrentPlayerTurn();
        }

        if (player1.getIsLoser()) {
            player2.sendMessage("You win!");
            player1.sendMessage("You lose");
        } else {
            player1.sendMessage("You win!");
            player2.sendMessage("You lose");
        }

        player1.sendMessage("Game ended");
        player2.sendMessage("Game ended");
    }

    private void sendBoards(ClientHandler player) {
        player.sendMessage(printBoards(player));
    }

    private void processPlayerMove(ClientHandler currentPlayer) {
        String move = currentPlayer.receiveMessage();
        int col = move.toUpperCase().charAt(0) - 'A';
        int row = Character.getNumericValue(move.charAt(1)) - 1;
        if (moveIsValid(move)) {
            char[][] opponentBoard = getOpponent(currentPlayer).getBoard();
            char field = opponentBoard[row][col];
            if (field == 'S') {
                opponentBoard[row][col] = 'X';
                currentPlayer.sendMessage("HIT!");
            } else {
                opponentBoard[row][col] = 'o';
                currentPlayer.sendMessage("MISS");
            }
        }
    }

    private boolean moveIsValid(String move) {
        String pattern = "^[A-Da-d][1-4]$";
        return Pattern.matches(pattern, move);
    }

    private void checkWinner() {
        isShipStillAlive(player1);
        isShipStillAlive(player2);
    }

    private void isShipStillAlive(ClientHandler player) {
        char[][] board = player.getBoard();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 'S') return;
            }
        }
        player.setLoser();
        gameOver = true;
    }

    private void switchCurrentPlayerTurn() {
        currentPlayerTurn = (currentPlayerTurn == player1) ? player2 : player1;
    }

    private ClientHandler getOpponent(ClientHandler player) {
        return (player == player1) ? player2 : player1;
    }


    private String printBoards(ClientHandler player) {
        StringBuilder builder = new StringBuilder();
        char[][] opponentBoard = getOpponent(player).getBoard();
        builder.append("\nOpponent: \n");
        setUpOpponentBoard(opponentBoard, builder);

        char[][] playerBoard = player.getBoard();
        builder.append("Yours: \n");
        setUpBoard(playerBoard, builder);

        return builder.toString();
    }

    private static void setUpBoard(char[][] board, StringBuilder builder) {
        appendLabel(builder);

        for (int i = 0; i < BOARD_SIZE; i++) {
            builder.append(i + 1);
            for (int j = 0; j < BOARD_SIZE; j++) {
                builder.append("|");
                builder.append(board[i][j]);
            }
            builder.append("| \n");
        }
    }

    private static void setUpOpponentBoard(char[][] board, StringBuilder builder) {
        appendLabel(builder);

        for (int i = 0; i < BOARD_SIZE; i++) {
            builder.append(i + 1);
            for (int j = 0; j < BOARD_SIZE; j++) {
                builder.append("|");
                if (board[i][j] == 'S') {
                    builder.append('-');
                } else {
                    builder.append(board[i][j]);
                }
            }
            builder.append("| \n");
        }
    }

    private static void appendLabel(StringBuilder builder) {
        builder.append(" |");
        for (int i = 0; i < BOARD_SIZE; i++) {
            char label = (char) ('A' + i);
            builder.append(label).append("|");
        }
        builder.append("\n");
    }
}
