package weather;

import org.json.simple.parser.ParseException;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;

public class SocketConnection {
  private static final String HOST = "api.openweathermap.org";
  private static final int PORT = 443;
  public static void main(String[] args) {
    SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

    try (Socket socket = factory.createSocket(HOST, PORT)) {
      OutputStream outputStream = socket.getOutputStream();
      PrintWriter writer = new PrintWriter(outputStream, true);

      InputStream inputStream = socket.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

      StringBuilder query = new StringBuilder("https://api.openweathermap.org/data/2.5/weather?");
      query.append("appid=").append("1ee4a04a324fede45569d2ce3873c5ac");
      query.append("&").append("units=metric");
      query.append("&").append("q=seoul");
      query.append("&").append("lang=kr");

      writer.println("GET " + query.toString() + " HTTP/1.1");
      writer.println("Host: " + HOST);
      writer.println("User-Agent: Chrome");
      writer.println("Connection: close");
      writer.println();

      StringBuilder response = new StringBuilder();
      String line;
      boolean isContent = false;
      while(!(line = reader.readLine()).isEmpty()) {}

      response.append(reader.readLine());
      System.out.println(response.toString());

      System.out.println(new Result(response.toString()));

    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }
}
