package ch.heigvd.pro.b04.auth.exceptions;

import ch.heigvd.pro.b04.error.PollClientException;
import org.springframework.http.HttpStatus;

public class CredentialsTooShortException extends PollClientException {

  public CredentialsTooShortException() {
    super("The poll credentials must be at least 4 characters long.", HttpStatus.BAD_REQUEST);
  }
}
