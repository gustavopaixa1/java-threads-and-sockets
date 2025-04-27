package TCP.ConcurrentServer;

import java.net.ServerSocket;
import java.net.Socket;

public class ThreadedServer {
      public static void main(String[] args) throws Exception {
            ServerSocket serverSocket = new ServerSocket(4460);
            System.out.println("Servidor concorrente pronto na porta 4444...");

            while (true) {
                  Socket clientSocket = serverSocket.accept();
                  System.out.println("Novo cliente conectado!");

                  // Cria uma nova thread para atender o cliente
                  Thread clientThread = new Thread(new ProtocolServer(clientSocket));
                  clientThread.start();
            }
      }
}
