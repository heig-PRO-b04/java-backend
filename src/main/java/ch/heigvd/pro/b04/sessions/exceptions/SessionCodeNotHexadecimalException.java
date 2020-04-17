package ch.heigvd.pro.b04.sessions.exceptions;

import ch.heigvd.pro.b04.error.PollClientException;
import org.springframework.http.HttpStatus;

public class SessionCodeNotHexadecimalException extends PollClientException {

  public SessionCodeNotHexadecimalException() {
    super("The provided session code is not valid.", HttpStatus.BAD_REQUEST);
  }
}
