package edu.wpi.DapperDaemons.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;

class AccountTest {

  @Test
  void checkPassword() throws NoSuchAlgorithmException {
    Account test = new Account("test", "1", "password");
    assertFalse(test.checkPassword("admin"));
    assertFalse(test.checkPassword("Password"));
    assertTrue(test.checkPassword("password"));
  }
}
