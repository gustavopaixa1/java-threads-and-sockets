package TCP.ProtocolClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ProtocolServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(4444);

        Socket ns = serverSocket.accept();
        Protocol protocol = new Protocol();

        DataInputStream inputStream = new DataInputStream(ns.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(ns.getOutputStream());

        while (true){
            String fromClient = inputStream.readUTF();
            System.out.println("Recebido do cliente: " + fromClient);
            outputStream.writeUTF(protocol.processLine(fromClient));
            if (fromClient.equals("Bye")) break;
        }
    }
}
