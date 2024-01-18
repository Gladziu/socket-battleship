package org.example.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    public Client(Socket socket) {
        this.socket = socket;
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345); // Podaj adres i port serwera
            Client client = new Client(socket);

            new Thread(() -> {
                while (true) {
                    client.receiveMessage();
                }
            }).start();

            while (true) {
                client.sendMessage();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendMessage() {
        try {
            Scanner scanner = new Scanner(System.in);
            String move = scanner.nextLine();

            bufferedWriter.write(move);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void receiveMessage() {
        try {
            String messageFromServer = bufferedReader.readLine();
            if (messageFromServer != null) {
                System.out.println(messageFromServer);
            } else {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
            System.exit(0); // Terminate the client when the connection is closed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
