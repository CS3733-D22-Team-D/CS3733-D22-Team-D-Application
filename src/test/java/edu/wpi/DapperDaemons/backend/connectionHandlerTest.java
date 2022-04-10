package edu.wpi.DapperDaemons.backend;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class connectionHandlerTest {

  @Test
  public void testConnections() throws IOException {
    connectionHandler.switchToClientServer();
  }
}
