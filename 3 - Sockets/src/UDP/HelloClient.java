package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class HelloClient {
      public static void main(String[] args) throws IOException {
            // Cria um socket UDP (cliente) sem se conectar a ninguém ainda
            DatagramSocket clientSocket = new DatagramSocket();

            // Prepara a mensagem a ser enviada (Hello Server)
            byte[] outBuffer = "Hello Server".getBytes();

            // Descobre o endereço do servidor (localhost no nosso caso)
            InetAddress serverAddress = InetAddress.getByName("localhost");

            // Cria um pacote UDP com a mensagem, o endereço do servidor e a porta 4444
            DatagramPacket outPacket = new DatagramPacket(outBuffer, outBuffer.length, serverAddress, 4444);

            // Envia o pacote para o servidor
            clientSocket.send(outPacket);

            // Prepara um buffer vazio para receber a resposta
            byte[] inBuffer = new byte[256];
            DatagramPacket inPacket = new DatagramPacket(inBuffer, inBuffer.length);

            // Espera receber um pacote de resposta (bloqueia até receber)
            clientSocket.receive(inPacket);

            // Extrai os dados recebidos e imprime
            System.out.println("Resposta do servidor: " + new String(inPacket.getData(), 0, inPacket.getLength()));

            // Fecha o socket UDP
            clientSocket.close();
      }
}
