package org.example.server;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class ClientHandler {
    private static final int BOARD_SIZE = 4;
    private char[][] board;
    private boolean isLoser;
    private final Socket clientSocket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ClientHandler(Socket socket) {
        this.board = initializeBoard();
        this.isLoser = false;
        this.clientSocket = socket;
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private char[][] initializeBoard() {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = '-';
            }
        }
        addShipOnRandomField();
        addShipOnRandomField();
        return board;
    }

    private void addShipOnRandomField() {
        Random random = new Random();
        int i, j;
        do {
            i = random.nextInt(BOARD_SIZE);
            j = random.nextInt(BOARD_SIZE);
        } while (board[i][j] == 'S');
        board[i][j] = 'S';
    }

    public char[][] getBoard() {
        return board;
    }

    public boolean getIsLoser() {
        return isLoser;
    }

    public void setLoser() {
        isLoser = true;
    }

    void sendMessage(String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(clientSocket, bufferedReader, bufferedWriter);
        }

    }

    public String receiveMessage() {
        String messageFromClient = null;
        try {
            messageFromClient = bufferedReader.readLine();
        } catch (IOException e) {
            closeEverything(clientSocket, bufferedReader, bufferedWriter);
        }
        return messageFromClient;
    }

    private void closeEverything(Socket clientSocket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
