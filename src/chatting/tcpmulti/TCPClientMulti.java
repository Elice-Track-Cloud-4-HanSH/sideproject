package chatting.tcpmulti;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPClientMulti {
  public static void main(String[] args) {
    String sentence;
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

    try (
      Socket clientSocket = new Socket("localhost", 6789);
      DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
      BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    ) {
      Thread rcvThread = new Thread( () -> {
        String modifiedSentence = null;
        while (true) {
          try {
            modifiedSentence = inFromServer.readLine();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
          System.out.println("서버로부터 응답: " + modifiedSentence);
        }
      });

      rcvThread.start();

      // ========= 입장할 채팅방 이름 입력 ==========
      // outToServer.write(~~~);
      // =========================================

      while (true) {
        System.out.print("서버에 보낼 메시지를 입력하세요: ");
        sentence = new String(inFromUser.readLine().getBytes(), StandardCharsets.UTF_8);
        outToServer.write((sentence + "\n").getBytes());
        if (sentence.equals("bye")) {
          rcvThread.join();
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
