package ch.heigvd.pro.b04.sessions.exceptions;

import ch.heigvd.pro.b04.error.exceptions.BadRequestException;

public class SessionCodeNotHexadecimalException extends BadRequestException {

  public SessionCodeNotHexadecimalException() {
    super("The provided session code is not valid.");
  }
}
