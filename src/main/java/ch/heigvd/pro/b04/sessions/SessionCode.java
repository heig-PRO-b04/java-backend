package ch.heigvd.pro.b04.sessions;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SessionCode {
  private static final String PATTERN = "0x[0-9A-F]+";
  private String hexadecimal;

  /**
   * Checks if the format of {@param codeReceived} is accepted.
   * @param codeReceived The {@link SessionCode} to verify
   * @return True if {@param codeReceived} conforms to a SessionCode, false otherwise
   */
  public static boolean conformsToFormat(SessionCode codeReceived) {
    return codeReceived.hexadecimal.matches(PATTERN);
  }
}
