package weather;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpConnection {
  public static void main(String[] args) {
    try {
      // qeury
      // id: city_id
      // appid: api_key
      StringBuilder query = new StringBuilder("?");
      query.append("appid=").append("1ee4a04a324fede45569d2ce3873c5ac");
      query.append("&").append("units=metric");
      query.append("&").append("q=seoul");
      query.append("&").append("lang=kr");

      URL url = new URL("https://api.openweathermap.org/data/2.5/weather" + query.toString());
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      connection.setRequestMethod("GET");
      connection.setRequestProperty("User-Agent", "Chrome");
      connection.setReadTimeout(30000);

      int responseCode = connection.getResponseCode();
      System.out.println("Http ResCode: " + responseCode);

      if (responseCode != HttpURLConnection.HTTP_OK) {
        System.out.println("커넥션 에러 발생");
        System.out.println(connection.getResponseMessage());
      }

      InputStream inputStream = connection.getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

      String line;
      StringBuilder sb = new StringBuilder();
      while ((line = bufferedReader.readLine()) != null) {
        System.out.println(line);
        sb.append(line);
      }
      bufferedReader.close();

      System.out.println(new Result(sb.toString()));

    } catch (MalformedURLException e) {
      System.out.println(e.getMessage());
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }
}
