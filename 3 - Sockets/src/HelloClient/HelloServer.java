package HelloClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HelloServer {
    static ServerSocket serverSocket;
    static Socket ns;
    static DataInputStream inputStream;
    static DataOutputStream outputStream;

    public static void main(String[] args) throws IOException {
        // Cria um servidor TCP que ouve na porta 4444
        serverSocket = new ServerSocket(4444);

        // O servidor fica bloqueado até que um cliente se conecte
        System.out.println("Esperando conexão...");
        ns = serverSocket.accept();


        inputStream = new DataInputStream(ns.getInputStream());
        outputStream = new DataOutputStream(ns.getOutputStream());

        // Lê a mensagem enviada pelo cliente e envia uma resposta
        System.out.println(inputStream.readUTF());
        outputStream.writeUTF("Hello Client");

        inputStream.close();
        outputStream.close();
        ns.close();
    }
}
