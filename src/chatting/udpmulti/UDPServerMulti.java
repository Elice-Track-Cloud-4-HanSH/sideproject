package chatting.udpmulti;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class UDPServerMulti {
  static Set<SocketAddress> clientPool = new HashSet<>();
  // 아래의 경우엔 채팅방별로 client를 넣을 수 있다.
  // static Map<String, Set<Socket>> clients = new HashMap<>();

  public static void main(String[] args) {
    try (DatagramSocket socket = new DatagramSocket(9876)) {
      System.out.println("클라이언트의 메시지를 대기하고 있습니다...");
      while (true) {
        byte[] rcvBuf = new byte[1024];
        // packet receiver set
        DatagramPacket rcvPacket = new DatagramPacket(rcvBuf, 0, rcvBuf.length);
        // receive packet
        socket.receive(rcvPacket);

        clientPool.add(rcvPacket.getSocketAddress());

        // get received message
        String rcvMessage = new String(rcvPacket.getData(), 0, rcvPacket.getLength(), StandardCharsets.UTF_8);
        System.out.printf("수신된 클라이언트 메시지: %s\n", rcvMessage);

        // 빈 문자열인 경우 send를 의도적으로 무시
        if (rcvMessage.isEmpty()) continue;
        // client pool에서 유저 삭제
        if (rcvMessage.equals("bye")) {
          clientPool.remove(rcvPacket.getSocketAddress());
          continue;
        }

        broadcast(socket, rcvMessage, rcvPacket.getSocketAddress());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static void broadcast(DatagramSocket socket, String message, SocketAddress origin) {
    byte[] sendData = (socket.getLocalPort() + ":" + message.toUpperCase()).getBytes();
    for (SocketAddress address : clientPool) {
      if (address.equals(origin)) continue;
      DatagramPacket packet = new DatagramPacket(sendData, sendData.length, address);
      try {
        socket.send(packet);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
