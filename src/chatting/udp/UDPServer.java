package chatting.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
  public static void main(String[] args) {
    try (DatagramSocket socket = new DatagramSocket(9876)) {
      System.out.println("클라이언트의 메시지를 대기하고 있습니다...");
      while (true) {
        byte[] rcvBuf = new byte[1024];
        // packet receiver set
        DatagramPacket rcvPacket = new DatagramPacket(rcvBuf, 0, rcvBuf.length);
        // receive packet
        socket.receive(rcvPacket);

        // print packet info
        System.out.printf("address: %s\nport: %d\n",
                  rcvPacket.getAddress(),
                  rcvPacket.getPort()
        );

        // get received message
        String rcvMessage = new String(rcvPacket.getData(), 0, rcvPacket.getLength());
        System.out.printf("수신된 클라이언트 메시지: %s\n\n", rcvMessage);
        if (rcvMessage.equals("bye")) break;

        byte[] sendData = rcvMessage.toUpperCase().getBytes();
        // packet sender set
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, rcvPacket.getAddress(), rcvPacket.getPort());
        // send packet
        socket.send(sendPacket);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
