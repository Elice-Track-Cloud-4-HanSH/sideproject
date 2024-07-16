package chatting.tcp;

import java.io.*;
import java.net.Socket;

public class TCPClient {
  public static void main(String[] args) {
    String sentence;
    String modifiedSentence;
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

    try (Socket clientSocket = new Socket("localhost", 6789)) {
      DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
      BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

      while (true) {
        System.out.print("서버에 보낼 메시지를 입력하세요: ");
        sentence = inFromUser.readLine();
        if (sentence.equals("bye")) break;

        outToServer.writeBytes(sentence + "\n");
        modifiedSentence = inFromServer.readLine();

        System.out.println("서버로부터 응답: " + modifiedSentence);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
