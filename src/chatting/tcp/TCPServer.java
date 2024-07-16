package chatting.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
  public static void main(String[] args) {
    int port = 6789;
    try (ServerSocket serverSocket = new ServerSocket(port)){
      // ==================== connection =======================
      System.out.println("서버가 " + port + " 포트에서 클라이언트의 연결을 기다립니다...");
      Socket connectionSocket = serverSocket.accept();
      System.out.println("클라이언트와 연결되었습니다.");
      System.out.printf("InetAddress: %s\nLocalAddress: %s\nport: %d\n",
                connectionSocket.getInetAddress(),
                connectionSocket.getLocalAddress(),
                connectionSocket.getPort()
      );

      BufferedReader in = new BufferedReader(
                new InputStreamReader(connectionSocket.getInputStream())
      );
      DataOutputStream out = new DataOutputStream(connectionSocket.getOutputStream());
      // ========================================================

      // 메시지 받는 부분만 while문 처리
      while (true) {
        String clientSentence = in.readLine();
        if (clientSentence.equals("bye")) break;
        System.out.println("클라이언트로부터 받은 메시지: " + clientSentence);

        String capitalizedSentence = clientSentence.toUpperCase() + "\n";
        out.writeBytes(capitalizedSentence);
      }
    } catch (IOException err) {
      err.getStackTrace();
    }
  }
}
