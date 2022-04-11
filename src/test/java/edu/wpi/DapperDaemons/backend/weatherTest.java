package edu.wpi.DapperDaemons.backend;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import org.junit.jupiter.api.Test;

class weatherTest {

  @Test
  void getWeather() throws Exception {
    Float res = weather.getWeather("boston");
    System.out.println(res);
  }
}
