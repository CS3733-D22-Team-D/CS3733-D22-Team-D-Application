package edu.wpi.DapperDaemons.backend;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class weatherTest {

  @Test
  void getWeather() throws Exception {
    Float res = weather.getWeather("boston");
    System.out.println(res);
  }
}
