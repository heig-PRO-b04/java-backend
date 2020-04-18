package ch.heigvd.pro.b04.error.exceptions;

import ch.heigvd.pro.b04.error.PollClientException;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends PollClientException {

  public ResourceNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }

  public ResourceNotFoundException() {
    this("The requested resource could not be found.");
  }
}
