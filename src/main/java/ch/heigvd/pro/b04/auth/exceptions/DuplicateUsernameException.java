package ch.heigvd.pro.b04.auth.exceptions;

import ch.heigvd.pro.b04.error.PollClientException;
import org.springframework.http.HttpStatus;

public class DuplicateUsernameException extends PollClientException {

  public DuplicateUsernameException() {
    super("This username already exists.", HttpStatus.BAD_REQUEST);
  }
}
