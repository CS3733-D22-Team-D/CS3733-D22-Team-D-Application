package edu.wpi.DapperDaemons.backend;

import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.*;

public class weather {
  public static void main(String[] args) throws IOException {
    URL url =
        new URL(
            "https://api.openweathermap.org/data/2.5/weather?lat=42.3601&lon=71.0589&appid=5214edbc18dc7cbd8c4c7abd482b6120&units=imperial");
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
    System.out.println(jsonString);
    JSONObject obj = new JSONObject(jsonString);
    JSONObject res = obj.getJSONArray("weather").getJSONObject(0);
    System.out.println(res.getString("description"));
    System.out.println(obj.getJSONObject("main").getFloat("temp"));
  }
}
