package ch.heigvd.pro.b04.auth.exceptions;

import ch.heigvd.pro.b04.error.exceptions.BadRequestException;

public class CredentialsTooShortException extends BadRequestException {

  public CredentialsTooShortException() {
    super("The poll credentials must be at least 4 characters long.");
  }
}

