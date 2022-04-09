package edu.wpi.DapperDaemons.backend;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class sms {

  // Find your Account Sid and Token at twilio.com/console
  public static final String ACCOUNT_SID = "ACc520cab845f2d60e76a26710d8035182";
  public static final String AUTH_TOKEN = "f1836f6191de387f2c5979f4f463fc7d";

  public static void main(String[] args) {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    Message message =
        Message.creator(
                new com.twilio.type.PhoneNumber("+19784826599"),
                "MG742ad9f2d4323b40b045ed3d603a4f04",
                "Hello, I can now send messages through java")
            .create();

    System.out.println(message.getSid());
  }
}
