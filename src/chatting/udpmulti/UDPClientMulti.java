package chatting.udpmulti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class UDPClientMulti {
  public static void main(String[] args) {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    try (DatagramSocket socket = new DatagramSocket()) {
      InetAddress IPAddress = InetAddress.getByName("localhost");

      Thread rcvThread = new Thread( () -> {
        try {
          while (true) {
            byte[] rcvData = new byte[1024];

            DatagramPacket rcvPacket = new DatagramPacket(rcvData, 0, rcvData.length);
            socket.receive(rcvPacket);

            String modifiedSentence = new String(rcvPacket.getData(), 0, rcvPacket.getLength());
            System.out.println("서버에서 받은 메시지: " + modifiedSentence);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
      rcvThread.start();




// ========= 입장할 채팅방 이름 입력 ==========
//chatroomName -> 사용자 입력값
      // packet sender set
      String chatroomName = "";
      DatagramPacket sendPacket = new DatagramPacket(chatroomName.getBytes(), 0, IPAddress, 9876);
      // send packet
      socket.send(sendPacket);
// =========================================
      while (true) {
        System.out.print("서버에 보낼 메시지: "); String sentence = br.readLine();
        byte[] sendData = sentence.getBytes();

        // packet sender set
        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
        // send packet
        socket.send(sendPacket);

        if (sentence.equals("bye"))  {
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
