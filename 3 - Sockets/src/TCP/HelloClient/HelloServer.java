package TCP.HelloClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HelloServer {
    public static void main(String[] args) throws Exception {

        // Cria um socket TCP no servidor, associado à porta 4444

        ServerSocket serverSocket = new ServerSocket(4444);  // bind + listen
        System.out.println("Servidor pronto, aguardando conexão...");

        // Aceita a conexão de um cliente (socket de acesso -> socket de conexão)
        Socket connectionSocket = serverSocket.accept();  // accept() bloqueia até um cliente se conectar
        System.out.println("Cliente conectado!");

        // Streams de entrada e saída (Camada de Aplicação usando a Camada de Transporte TCP)
        DataInputStream in = new DataInputStream(connectionSocket.getInputStream());
        DataOutputStream out = new DataOutputStream(connectionSocket.getOutputStream());

        // Recebe a mensagem do cliente
        String messageFromClient = in.readUTF();
        System.out.println("Recebido do cliente: " + messageFromClient);

        // Responde ao cliente
        out.writeUTF("Hello Client");

        // Fecha streams e sockets (finalizando o endpoint)
        in.close();
        out.close();
        connectionSocket.close();
        serverSocket.close();
    }
}
