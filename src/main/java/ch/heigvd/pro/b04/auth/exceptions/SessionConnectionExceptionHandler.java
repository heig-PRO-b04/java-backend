package ch.heigvd.pro.b04.auth.exceptions;

import ch.heigvd.pro.b04.error.exceptions.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class SessionConnectionExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(SessionNotAvailableException.class)
  public ResponseEntity<ErrorResponse> sessionNotAvailable() {
    return new ResponseEntity<>(ErrorResponse.from("This session exists but it is closed"
    ), HttpStatus.FORBIDDEN);
  }

  /**
   * Returns an {@link ErrorResponse} if a duplicate username exception is triggered.
   */
  @ExceptionHandler(SessionNotExistingException.class)
  public ResponseEntity<ErrorResponse> sessionNotExisting() {
    return new ResponseEntity<>(
        ErrorResponse.from("This code does not exist"),
        HttpStatus.FORBIDDEN
    );
  }
}