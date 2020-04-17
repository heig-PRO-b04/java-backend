package ch.heigvd.pro.b04.auth.exceptions;

import ch.heigvd.pro.b04.error.PollClientException;

public class DuplicateUsernameException extends PollClientException {

  public DuplicateUsernameException() {
    super("This username already exists.", 400);
  }
}
