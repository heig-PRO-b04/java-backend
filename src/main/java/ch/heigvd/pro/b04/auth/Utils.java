package ch.heigvd.pro.b04.auth;

import ch.heigvd.pro.b04.Constants;
import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;

public class Utils {

  public static String hash(String password) {
    return Hashing.sha512()
        .hashString(password + Constants.HASH, StandardCharsets.UTF_8)
        .toString();
  }
}
