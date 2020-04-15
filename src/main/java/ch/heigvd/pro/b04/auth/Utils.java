package ch.heigvd.pro.b04.auth;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;

public class Utils {

  private static final String SALT = "Tu-144";

  /**
   * Hashes a password string with a SHA512 function. Currently, a constant salt is used.
   *
   * @param password The String password to hash.
   * @return A hashed, UTF-8 encoded version of the input.
   */
  public static String hash(String password) {
    return Hashing.sha512()
        .hashString(password + SALT, StandardCharsets.UTF_8)
        .toString();
  }
}
