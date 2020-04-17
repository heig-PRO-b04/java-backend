package ch.heigvd.pro.b04.error.exceptions;

import ch.heigvd.pro.b04.error.PollClientException;

public class ResourceNotFoundException extends PollClientException {

  public ResourceNotFoundException() {
    super("The requested resource could not be found.", 404);
  }
}
