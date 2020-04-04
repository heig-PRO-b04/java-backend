package ch.heigvd.pro.b04.error.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {

  private String message;

  public static ErrorResponse from(String message) {
    return new ErrorResponse(message);
  }
}
