package edu.wpi.DapperDaemons.backend;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import java.io.*;

public class FireBase {

  private static DatabaseReference ref;

  private FireBase() {}

  public static DatabaseReference getReference() {
    return ref;
  }

  public static void init() {
    InputStream serviceAccount = null;
    InputStream targetStream;
    //    JsonObject acc =
    //        JsonParser.parseReader(
    //                new InputStreamReader(
    //                    Objects.requireNonNull(
    //                        FireBase.class
    //                            .getClassLoader()
    //                            .getResourceAsStream("service-acount.json"))))
    //            .getAsJsonObject();
    //    targetStream = new ByteArrayInputStream(acc.getAsString().getBytes());
    //    String jsonString = "{\n" +
    //            "  \"type\": \"service_account\",\n" +
    //            "  \"project_id\": \"bwh-application\",\n" +
    //            "  \"private_key_id\": \"acbf2d92da9ed6dac6e95a027cccc09cb78bbbb0\",\n" +
    //            "  \"private_key\": \"-----BEGIN PRIVATE
    // KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCzjCHJgopPX7ek\\nLn9UwYPHjHzCvcyrxGwmy53jI3VrrbuPu/EFIAgtosZrKFWenslnBCP1Kuut3cTs\\nCAVEsPyTzsj/zCKDXDSWMszxJYLTBaF2v5WLgNzypa4vfUacv7kj9MkZRmsJEFfm\\nm72c6HGUlS49wS8pQeOUXAtSAoMfJJpSNrW3raLnc6CSAsB4dBBm5bK00/m0jy4E\\n7AMHAkjvw9ZdahULWU4f7YlN4jwzORmja9nV2V/xNfkpE763dFBoCJrJOUzRu/Uc\\nOfBRUQptKomNhbwY1W7sehyc4uZalMdct3fnvwJ7wV9+LyH0FNjGa2rej9ULPmqF\\nj3A/YPsjAgMBAAECggEAD3JUpprZCuxOLUBlN5J3PUZEP+P6S4pYT5u6F2qoIgpn\\nr31YxlSzv2ryLRF8RDg4iLW3ewXyEedmMKdqZJ16+iOSvhPLoe3fbE767xtUccXz\\nPSpNIE8gZkbqTfwC6vgnlZ6FyoUSlX+j2egeKPVqboD8rtcDsRLFFH3SteYXbijs\\nkEy80J4GMi6qe0+9qIHR7O+Fiu/xssNT5Y6BM9Ys2kEWlyAsZyaLUrodVg7U2hdE\\n2OfFvUMHRJhWD/i58xD9+OB/JlBG9MmSYzJXOC678k155LDIuyiVhAzO28Tn2Iu9\\nnM5O9BB1HmHThLMk0QNYxC2/w2DvXJUK3nGFny++YQKBgQD1VH0PF5jJ9RFTjd8r\\nd4kJ/YK4ravFS6cPXMWW5JalYyaC2O76vqlTRt2cqY/uxRbFJ4dTz7bZ+DTzGQee\\n7iS0BdThrDPrqGxJYRAzdNELq/k6fto3k/YpXJC4s4ZEh9MWahskYyj1MKb/OfF1\\n7gpKK6gS59FebCQWylF3CZwWkQKBgQC7Wze5nZUYuMWZA+3aBoIjlzTu0caHYJG6\\n9na8YEWFsJ0tzcicj0MRs4nE6gTa/v8UqhrDBNZ2/PQ535w7lVf0WOZqhIGA3CLE\\nUId2CvNxqbCv3IDmYvoaOfuCx1QF0edTC15qGCQyO5PBi260IC/Vyw8nWRFSAH7V\\nESOLiPFYcwKBgEWMXSOxKdRfEDlip9i3u3bEkpFuUprM9qBJSY6cYQSj1iAUBbO5\\n52C/X0XxX+dtoiXkLd4ZrZmfXstn1hw9INSULqSHX/S3u3MV/DTKMhUf/LiTi6Nj\\n484X+sMRyceVxca979Hh9pOD5mPGdmhbQjmCpNQvOdCN0bFQS8X2SHSxAoGBAKRP\\nDCDhvTcwHELkej0CBjG2Uvq6yFRDcaq4qUrtVys2x7G6zhvXXCdy//jMjzOL/CKg\\nR9YOMROrJXh3zalHgRpOuMxjprte07K9OP9h/TwKaGTiib0CwnorrZJ0wxBTlH50\\n8vkycNQJAbz0TkL0E194tLJ6X0E+I/0yCa0+jQJXAoGAfzxuuyFqzalU2knfCHJB\\nVMQcnJMsjm2x5FQNPWtLuRkO/SjAuMdrGNcnUJO5fMiIvPHZy512y7jtQqCOroOz\\nB5wGGmBpB+/S3VSNSG8etQR1wBij1qdKKi7qLbuC6kiK8UNaXaQoJUDfpuuESGOA\\n6W0AW1GwSkaVVXAkaffMCWc=\\n-----END PRIVATE KEY-----\\n\",\n" +
    //            "  \"client_email\":
    // \"firebase-adminsdk-zo6ha@bwh-application.iam.gserviceaccount.com\",\n" +
    //            "  \"client_id\": \"107805596325432165977\",\n" +
    //            "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
    //            "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
    //            "  \"auth_provider_x509_cert_url\":
    // \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
    //            "  \"client_x509_cert_url\":
    // \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-zo6ha%40bwh-application.iam.gserviceaccount.com\"\n" +
    //            "}\n";
    //    JsonObject acc = JsonParser.parseString(jsonString);
    //    try {
    //      //      serviceAccount = new
    //     FileInputStream("C:/Users/jrmad/Downloads/service-account.json");
    serviceAccount = FireBase.class.getClassLoader().getResourceAsStream("service-account.json");
    //
    //    } catch (FileNotFoundException e) {
    //      System.out.println("Service account json not found");
    //    }

    FirebaseOptions options = null;
    // Initialize the app with a service account, granting admin privileges
    try {
      options =
          FirebaseOptions.builder()
              .setCredentials(GoogleCredentials.fromStream(serviceAccount))
              // The database URL depends on the location of the database
              .setDatabaseUrl("https://bwh-application-default-rtdb.firebaseio.com/")
              .build();
    } catch (IOException e) {
      System.out.println("Connection to the database could not be secured");
    }

    if (options != null) {
      FirebaseApp.initializeApp(options);

      ref = FirebaseDatabase.getInstance().getReference();
    }
  }
}
