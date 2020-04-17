package ch.heigvd.pro.b04.error.exceptions;

import ch.heigvd.pro.b04.error.PollClientException;

public class ForbiddenException extends PollClientException {

  public ForbiddenException() {
    super("Access to this resource is forbidden.", 403);
  }
}
