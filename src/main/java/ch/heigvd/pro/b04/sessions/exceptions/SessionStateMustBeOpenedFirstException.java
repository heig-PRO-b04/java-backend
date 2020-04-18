package ch.heigvd.pro.b04.sessions.exceptions;

import ch.heigvd.pro.b04.error.exceptions.BadRequestException;

public class SessionStateMustBeOpenedFirstException extends BadRequestException {

  public SessionStateMustBeOpenedFirstException() {
    super("You have to open a session before changing it's status");
  }
}
