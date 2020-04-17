package ch.heigvd.pro.b04.sessions.exceptions;

import ch.heigvd.pro.b04.error.PollClientException;

public class SessionCodeNotHexadecimalException extends PollClientException {

  public SessionCodeNotHexadecimalException() {
    super("The provided session code is not valid.", 400);
  }
}
