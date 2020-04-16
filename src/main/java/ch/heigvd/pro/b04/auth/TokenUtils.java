package ch.heigvd.pro.b04.auth;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * A class with different static helpers for token-related logic.
 */
public class TokenUtils {

  private static final int SALT_LENGTH = 64;
  private static final int TOKEN_LENGTH = 64;
  private static final SecureRandom sRandom = new SecureRandom();

  private TokenUtils() {
    /* No instances. */
  }

  /**
   * Returns some cryptographically secure and randomly generated salt.
   */
  /* package */
  static byte[] generateRandomSalt() {
    byte[] values = new byte[SALT_LENGTH];
    sRandom.nextBytes(values);
    return values;
  }

  /**
   * Returns some cryptographically secure token, that can be used to authenticate users.
   */
  /* package */
  static byte[] generateRandomToken() {
    byte[] values = new byte[TOKEN_LENGTH];
    sRandom.nextBytes(values);
    return values;
  }

  /**
   * Encodes a provided source into its base 64 representation.
   *
   * @param source The source to encode.
   * @return The encoded String.
   */
  /* package */
  static String base64Encode(byte[] source) {
    return Base64.getUrlEncoder().encodeToString(source);
  }

  /**
   * Decodes a provided source from its base 64 representation.
   *
   * @param source The source to decode.
   * @return The decoded byte array.
   */
  /* package */
  static byte[] base64Decode(String source) {
    return Base64.getUrlDecoder().decode(source);
  }

  /**
   * Returns a secured secret, based on some salt and an associated password. This function
   * is deterministic, and it is therefore extremely important that the provided salt is
   * cryptographically secure !
   *
   * A secret does not replace an authentication token, but instead ensures that if the database is
   * accessed, the password can not be easily reversed.
   *
   * @param password The password that is used for generating the hash.
   * @param salt     The salt that is applied to the hash.
   * @return The generated token.
   */
  public static String getSecret(String password, byte[] salt) {
    return Hashing.sha256()
        .newHasher()
        .putBytes(salt)
        .putBytes(password.getBytes(StandardCharsets.UTF_8))
        .hash()
        .toString();
  }
}
