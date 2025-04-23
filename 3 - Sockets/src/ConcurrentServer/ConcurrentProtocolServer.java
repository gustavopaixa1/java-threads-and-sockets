package ConcurrentServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConcurrentProtocolServer {
    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(4445);
        System.out.println("Server started on port 4444");

        while (true) {
            Socket clientSocket = ss.accept();
            System.out.println("Novo cliente conectado: " + clientSocket.getInetAddress());

            Thread clientThread = new Thread(new ClientHandler(clientSocket));
        }
    }
}
