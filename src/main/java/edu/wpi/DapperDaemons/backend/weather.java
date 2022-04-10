package edu.wpi.DapperDaemons.backend;

import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.*;

public class weather {

  private weather() {}

  public static String[] getWeather(String location) throws Exception {
    URL url =
        new URL(
            "https://api.openweathermap.org/data/2.5/weather?q="
                + location
                + "&appid=5214edbc18dc7cbd8c4c7abd482b6120&units=imperial");
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");

    int status = con.getResponseCode();

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer content = new StringBuffer();
    while ((inputLine = in.readLine()) != null) {
      content.append(inputLine);
    }
    in.close();
    con.disconnect();

    String jsonString = content.toString(); // assign your JSON String here
    String[] ret = new String[2];
    JSONObject obj = new JSONObject(jsonString);
    JSONObject res = obj.getJSONArray("weather").getJSONObject(0);
    ret[0] = res.getString("description");
    ret[1] = String.valueOf(obj.getJSONObject("main").getFloat("temp"));
    return ret;
  }
}
