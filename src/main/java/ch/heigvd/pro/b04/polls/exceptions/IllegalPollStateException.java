package ch.heigvd.pro.b04.polls.exceptions;

public class IllegalPollStateException extends IllegalStateException {
  public IllegalPollStateException() {
    super("Impossible state occured. More than one poll with the same id");
  }
}
