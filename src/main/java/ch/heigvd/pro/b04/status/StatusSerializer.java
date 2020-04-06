package ch.heigvd.pro.b04.status;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class StatusSerializer extends JsonSerializer<Status> {

  @Override
  public void serialize(
      Status status,
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider
  ) throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeStringField("message", status.getMessage());
    jsonGenerator.writeEndObject();
  }
}
