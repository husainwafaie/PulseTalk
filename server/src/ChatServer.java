import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ChatServer {
    private static final int PORT = 1234; // main port for now
    private static ArrayList<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("server started");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");
                
                ClientHandler clientHandler = new ClientHandler(clientSocket, clients);
                clients.add(clientHandler);
                
                new Thread(clientHandler).start();  // start process
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }
}
