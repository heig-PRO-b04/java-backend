package ch.heigvd.pro.b04.auth.exceptions;

import ch.heigvd.pro.b04.error.exceptions.BadRequestException;

public class DuplicateUsernameException extends BadRequestException {

  public DuplicateUsernameException() {
    super("This username already exists.");
  }
}
