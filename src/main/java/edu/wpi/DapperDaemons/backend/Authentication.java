package edu.wpi.DapperDaemons.backend;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import edu.wpi.DapperDaemons.entities.Account;

import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

public class Authentication {

  // Find your Account Sid and Token at twilio.com/console
  public static final String ACCOUNT_SID = "ACc520cab845f2d60e76a26710d8035182";
  public static final String AUTH_TOKEN = "f1836f6191de387f2c5979f4f463fc7d";
  private static int authCode;
  private static DAO<Account> accounts = DAOPouch.getAccountDAO();

  private static void generateCode(){
    authCode = ThreadLocalRandom.current().nextInt(100000, 1000000);
    Thread timer = new Thread(new Runnable() {
      @Override
      public void run() {
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start < 5000);
        authCode = -1;
      }
    });
  }

  public static boolean authenticate(int code){
    if(code != -1){
      return code == authCode;
    }
    return false;
  }

  public static void sendAuthCode() throws SQLException {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    Account acc = accounts.get(SecurityController.user.getAttribute(1));
    generateCode();
    Message message =
            Message.creator(
                            new com.twilio.type.PhoneNumber(acc.getAttribute(4)),
                            "MG742ad9f2d4323b40b045ed3d603a4f04",
                            "Your verification code is:\n" + authCode)
                    .create();

    System.out.println(message.getSid());
  }
}
