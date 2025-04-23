package HelloClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HelloClient {
    static Socket socket;
    static DataInputStream inputStream;
    static DataOutputStream outputStream;

    public static void main(String[] args) throws IOException {
        // O cliente se conecta ao servidor na porta 4444 da maquina local
        socket = new Socket("localhost", 4444);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());

        // Envia mensagem ao servidor através do output stream
        outputStream.writeUTF("Hello Server");

        //Imprime o que o servidor enviou
        System.out.println(inputStream.readUTF());

        inputStream.close();
        outputStream.close();
        socket.close();
    }
}


