package org.example.server;

import java.util.ArrayList;
import java.util.List;

public class WaitingRoom {
    private static final int MAX_PLAYERS = 2;
    private static final List<ClientHandler> waitingPlayers = new ArrayList<>();

    public static synchronized void addPlayer(ClientHandler player) {
        waitingPlayers.add(player);
        if (waitingPlayers.size() == MAX_PLAYERS) {
            startGame();
        }
    }

    public static synchronized void removePlayer(ClientHandler player) {
        waitingPlayers.remove(player);
    }

    private static void startGame() {
        Game game = new Game(waitingPlayers.get(0), waitingPlayers.get(1));
        new Thread(game).start();
        waitingPlayers.clear();
    }
}
