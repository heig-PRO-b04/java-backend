package ch.heigvd.pro.b04.tea.exceptions;

import ch.heigvd.pro.b04.error.PollClientException;
import org.springframework.http.HttpStatus;

public class TeapotException extends PollClientException {

  public TeapotException() {
    super("We are a teapot, we cannot brew coffee.", HttpStatus.I_AM_A_TEAPOT);
  }
}
