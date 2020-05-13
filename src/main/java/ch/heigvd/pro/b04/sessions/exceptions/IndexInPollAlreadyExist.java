package ch.heigvd.pro.b04.sessions.exceptions;

import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;

public class IndexInPollAlreadyExist extends ResourceNotFoundException {

  public IndexInPollAlreadyExist() {
    super("Index in poll already exist");
  }
}
