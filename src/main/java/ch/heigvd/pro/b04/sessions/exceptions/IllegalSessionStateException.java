package ch.heigvd.pro.b04.sessions.exceptions;

public class IllegalSessionStateException extends IllegalStateException {
  public IllegalSessionStateException() {
    super("Error in session management");
  }
}
