package ch.heigvd.pro.b04.sessions.exceptions;

import ch.heigvd.pro.b04.error.exceptions.ForbiddenException;

public class SessionNotAvailableException extends ForbiddenException {
  public SessionNotAvailableException() {
    super("The session is no longer open to new connections.");
  }
}
