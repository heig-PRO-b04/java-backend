package ch.heigvd.pro.b04.error.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ResourceNotFoundExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Returns an {@link ErrorResponse} for resources that are missing, and have been indicated as
   * such by the endpoints that throw the associated {@link ResourceNotFoundException}.
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> resourceNotFound() {
    return new ResponseEntity<>(
        ErrorResponse.from("Resource not found."),
        HttpStatus.NOT_FOUND
    );
  }
}
