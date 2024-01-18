package org.example.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 12345;
    private static final List<ClientHandler> clients = new ArrayList<>();
    private static final WaitingRoom waitingRoom = new WaitingRoom();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.sendMessage("Welcome to BattleShips Game !");
                clientHandler.sendMessage("Legend: '-' - possible ship, 'S' - ship, 'o' - miss, 'X' - hit ");
                clients.add(clientHandler);

                waitingRoom.addPlayer(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
