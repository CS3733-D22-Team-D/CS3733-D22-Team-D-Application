package edu.wpi.DapperDaemons.backend;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class weatherTest {

  @Test
  void getWeather() throws Exception {
    String[] res = weather.getWeather("boston");
    System.out.println(res[0]);
    System.out.println(res[1]);
  }
}
