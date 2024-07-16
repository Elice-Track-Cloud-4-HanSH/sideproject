package chatting.tcpmulti;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class TCPServerMulti {
  static Set<Socket> clients = new HashSet<>();
  public static void main(String[] args) {
    int port = 6789;
    try (ServerSocket serverSocket = new ServerSocket(port)){
      while (true) {
        System.out.println("서버가 " + port + " 포트에서 클라이언트의 연결을 기다립니다...");
        Socket client = serverSocket.accept();
        clients.add(client);
        System.out.println("클라이언트와 연결되었습니다. - " + client.getLocalSocketAddress());
        new Thread(() -> {
          handleClient(client);
        }).start();
        System.out.println("   connected - " + client + "\n        left - " + clients);
      }
    } catch (IOException err) {
      err.getStackTrace();
    }
  }

  static void handleClient(Socket client) {
    try (
      BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    ) {
      String message;
      while (true) {
        message = new String(in.readLine().getBytes(), StandardCharsets.UTF_8);
        System.out.println(message);
        if (message.isEmpty()) continue;
        if (message.equals("bye")) {
          clients.remove(client);
          client.close();
          break;
        }
        broadcast(client, message);
      }
    } catch (IOException e) {
      clients.remove(client);
    } finally {
      System.out.println("disconnected - " + client + "\n        left - " + clients);
    }
  }

  static void broadcast(Socket origin, String message) {
    for (Socket client : clients) {
      if (client.equals(origin)) continue;
      try {
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        out.write((message.toUpperCase() + "\n").getBytes());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
