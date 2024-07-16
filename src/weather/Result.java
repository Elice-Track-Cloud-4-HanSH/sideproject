package weather;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Result {
  String city;
  double lon;
  double lat;
  String weather;
  double temp;
  long humidity;

  public Result() {}
  public Result(String jsonedString) throws ParseException {
    JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonedString);
    this.makeResult(jsonObject);
  }
  public Result(JSONObject jsonObject) {
    this.makeResult(jsonObject);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("=====").append(this.city).append("=====\n");
    sb.append("위도: ").append(this.lon).append("\n");
    sb.append("경도: ").append(this.lat).append("\n");
    sb.append("날씨: ").append(this.weather).append("\n");
    sb.append("기온: ").append(this.temp).append("\n");
    sb.append("습도: ").append(this.humidity).append("\n");
    return sb.toString();
  }

  public void makeResult(JSONObject jsonObject) {
    this.lon = (double)((JSONObject) jsonObject.get("coord")).get("lon");
    this.lat = (double)((JSONObject) jsonObject.get("coord")).get("lat");
    this.city = (String)jsonObject.get("name");
    this.weather = (String)((JSONObject) ((JSONArray) jsonObject.get("weather")).get(0)).get("description");
    this.temp = (double)((JSONObject) jsonObject.get("main")).get("temp");
    this.humidity = (long)((JSONObject) jsonObject.get("main")).get("humidity");
  }
}
