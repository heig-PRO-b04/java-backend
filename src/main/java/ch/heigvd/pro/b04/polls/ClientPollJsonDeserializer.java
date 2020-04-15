package ch.heigvd.pro.b04.polls;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class ClientPollJsonDeserializer extends JsonDeserializer<ClientPoll> {

  @Override
  public ClientPoll deserialize(
      JsonParser jsonParser,
      DeserializationContext deserializationContext
  ) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    String title = node.get("title").asText();
    return ClientPoll.builder()
        .title(title)
        .build();
  }
}
