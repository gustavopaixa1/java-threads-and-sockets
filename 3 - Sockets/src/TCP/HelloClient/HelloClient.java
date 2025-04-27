package TCP.HelloClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class HelloClient {
    public static void main(String[] args) throws Exception {
        // Cria um socket TCP para conectar ao servidor na porta 4444
        Socket clientSocket = new Socket("localhost", 4444);  // connect()

        // Streams de entrada e saída para comunicação com o servidor
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        DataInputStream in = new DataInputStream(clientSocket.getInputStream());

        // Envia mensagem para o servidor
        out.writeUTF("Hello Server");

        // Recebe resposta do servidor
        String messageFromServer = in.readUTF();
        System.out.println("Recebido do servidor: " + messageFromServer);

        // Fecha streams e socket (encerra a conexão TCP)
        in.close();
        out.close();
        clientSocket.close();
    }
}
