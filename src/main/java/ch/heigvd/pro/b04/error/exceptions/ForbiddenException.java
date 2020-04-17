package ch.heigvd.pro.b04.error.exceptions;

import ch.heigvd.pro.b04.error.PollClientException;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends PollClientException {

  public ForbiddenException() {
    super("Access to this resource is forbidden.", HttpStatus.FORBIDDEN);
  }
}
