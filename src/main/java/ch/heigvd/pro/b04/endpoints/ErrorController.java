package ch.heigvd.pro.b04.endpoints;

import ch.heigvd.pro.b04.endpoints.exceptions.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

  private static final String ERROR_PATH = "/error";

  @RequestMapping("/error")
  public ResponseEntity<ErrorResponse> notFound() {
    return new ResponseEntity<>(
        ErrorResponse.from("This resource does not exist."),
        HttpStatus.NOT_FOUND
    );
  }

  @Override
  public String getErrorPath() {
    return ErrorController.ERROR_PATH;
  }
}
