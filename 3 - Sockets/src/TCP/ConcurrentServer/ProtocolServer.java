package TCP.ConcurrentServer;

import TCP.ProtocolClient.Protocol;

import java.io.*;
import java.net.Socket;

public class ProtocolServer implements Runnable {
      Socket clientSocket;

      public ProtocolServer(Socket socket) {
            this.clientSocket = socket;
      }

      public void run() {
            try {
                  DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                  DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                  Protocol protocol = new Protocol(); // Classe para interpretar comandos

                  while (true) {
                        String fromClient = in.readUTF();
                        System.out.println("Recebido do cliente: " + fromClient);
                        String response = protocol.processLine(fromClient);
                        out.writeUTF(response);

                        if (fromClient.equals("Bye")) break;
                  }

                  in.close();
                  out.close();
                  clientSocket.close();
            } catch (IOException e) {
                  System.out.println("Erro de comunicação: " + e.getMessage());
            }
      }
}
