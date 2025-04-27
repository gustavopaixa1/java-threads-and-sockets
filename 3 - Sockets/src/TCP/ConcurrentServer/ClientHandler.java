package TCP.ConcurrentServer;

import TCP.ProtocolClient.Protocol;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
      private final Socket clientSocket;
      private final Protocol protocol;

      public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            this.protocol = new Protocol();
      }

      @Override
      public void run() {
            try (
                  DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
                  DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream())
            ) {
                  String fromClient;
                  do {
                        fromClient = inputStream.readUTF();
                        System.out.println("Cliente (" + clientSocket.getInetAddress() + "): " + fromClient);

                        String response = protocol.processLine(fromClient);
                        outputStream.writeUTF(response);
                  } while (!fromClient.equals("Bye"));

                  System.out.println("Cliente desconectado: " + clientSocket.getInetAddress());

            } catch (IOException e) {
                  System.out.println("Erro com cliente: " + e.getMessage());
            } finally {
                  try {
                        clientSocket.close();
                  } catch (IOException e) {
                        System.out.println("Erro ao fechar socket do cliente.");
                  }
            }
      }
}
