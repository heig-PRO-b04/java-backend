package ch.heigvd.pro.b04.messages;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.jackson.JsonComponent;

/**
 * A class that is useful to transmit server messages to the client.
 */
@Builder
@Data
public class ServerMessage {

  private String message;

  @JsonComponent
  public static class JsonSerializer extends
      com.fasterxml.jackson.databind.JsonSerializer<ServerMessage> {

    @Override
    public void serialize(
        ServerMessage message,
        JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider
    ) throws IOException {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeStringField("message", message.getMessage());
      jsonGenerator.writeEndObject();
    }
  }
}
