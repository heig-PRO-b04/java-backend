package ch.heigvd.pro.b04.error.exceptions;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class ErrorResponseSerializer extends JsonSerializer<ErrorResponse> {

  @Override
  public void serialize(
      ErrorResponse errorResponse,
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider
  ) throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeStringField("message", errorResponse.getMessage());
    jsonGenerator.writeEndObject();
  }
}
