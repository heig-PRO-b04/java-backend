package ch.heigvd.pro.b04.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class PollClientExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(PollClientException.class)
  public ResponseEntity<PollClientException> handlePollClientException(PollClientException e) {
    return new ResponseEntity<>(e, e.getStatus());
  }
}
