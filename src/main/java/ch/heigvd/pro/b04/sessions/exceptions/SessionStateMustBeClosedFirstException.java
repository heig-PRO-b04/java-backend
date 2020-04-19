package ch.heigvd.pro.b04.sessions.exceptions;

import ch.heigvd.pro.b04.error.exceptions.BadRequestException;

public class SessionStateMustBeClosedFirstException extends BadRequestException {

  public SessionStateMustBeClosedFirstException() {
    super("You have to close the previous session before opening a new one");
  }
}
