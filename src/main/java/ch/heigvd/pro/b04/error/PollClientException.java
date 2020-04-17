package ch.heigvd.pro.b04.error;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.springframework.boot.jackson.JsonComponent;

public class PollClientException extends Exception {

  private final int code;

  public PollClientException(String message, int code) {
    super(message);
    this.code = code;
  }

  public int getCode() {
    return code;
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
