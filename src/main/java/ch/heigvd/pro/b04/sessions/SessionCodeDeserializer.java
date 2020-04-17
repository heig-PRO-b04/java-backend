package ch.heigvd.pro.b04.sessions;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class SessionCodeDeserializer extends JsonDeserializer<SessionCode> {

  @Override
  public SessionCode deserialize(
      JsonParser jsonParser,
      DeserializationContext deserializationContext
  ) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    String hexadecimal = node.get("code").asText();
    return SessionCode.builder()
        .hexadecimal(hexadecimal)
        .build();
  }
}
