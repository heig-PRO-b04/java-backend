package ch.heigvd.pro.b04.polls.exceptions;

import ch.heigvd.pro.b04.error.exceptions.BadRequestException;

public class PollNotExistingException extends BadRequestException {
  public PollNotExistingException() {
    super("Given poll doesn't exist");
  }
}
