package chatting.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
  public static void main(String[] args) {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    try (DatagramSocket socket = new DatagramSocket()) {
      InetAddress IPAddress = InetAddress.getByName("localhost");

      while (true) {
        System.out.print("서버에 보낼 메시지: "); String sentence = br.readLine();
        byte[] sendData = sentence.getBytes();

        // packet sender set
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
        // send packet
        socket.send(sendPacket);

        if (sentence.equals("bye"))  {
          break;
        }

        // packet receiver set
        byte[] rcvData = new byte[1024];
        DatagramPacket rcvPacket = new DatagramPacket(rcvData, 0, rcvData.length);
        // packet receive
        socket.receive(rcvPacket);

        String modifiedSentence = new String(rcvPacket.getData(), 0, rcvPacket.getLength());
        System.out.println("서버에서 받은 메시지: " + modifiedSentence);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
