package ch.heigvd.pro.b04.error;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.http.HttpStatus;

public class PollClientException extends Exception {

  private final HttpStatus status;

  public PollClientException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  public HttpStatus getStatus() {
    return status;
  }

  @JsonComponent
  public static class Serializer extends JsonSerializer<PollClientException> {

    @Override
    public void serialize(
        PollClientException exception,
        JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider
    ) throws IOException {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeStringField("error", exception.getMessage());
      jsonGenerator.writeEndObject();
    }
  }
}
