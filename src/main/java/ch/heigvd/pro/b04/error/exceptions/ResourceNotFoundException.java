package ch.heigvd.pro.b04.error.exceptions;

import ch.heigvd.pro.b04.error.PollClientException;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends PollClientException {

  public ResourceNotFoundException() {
    super("The requested resource could not be found.", HttpStatus.NOT_FOUND);
  }
}
