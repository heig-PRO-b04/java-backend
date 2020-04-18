package ch.heigvd.pro.b04.sessions.exceptions;

import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;

public class SessionNotExistingException extends ResourceNotFoundException {
  public SessionNotExistingException() {
    super("The requested session does not exist.");
  }
}
