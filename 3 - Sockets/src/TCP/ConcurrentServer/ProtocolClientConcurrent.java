package TCP.ConcurrentServer;

import java.io.*;
import java.net.Socket;

public class ProtocolClientConcurrent {
      public static void main(String[] args) throws IOException {
            Socket s = new Socket("localhost", 4460);

            DataInputStream inputStream = new DataInputStream(s.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(s.getOutputStream());
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                  System.out.print("Você: ");
                  String fromUser = stdIn.readLine();
                  outputStream.writeUTF(fromUser);
                  System.out.println(inputStream.readUTF());
                  if (fromUser.equals("Bye")) {
                        break;

                  }
            }
            inputStream.close();
            outputStream.close();
            s.close();

      }
}
