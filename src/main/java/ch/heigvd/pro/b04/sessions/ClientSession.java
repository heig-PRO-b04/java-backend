package ch.heigvd.pro.b04.sessions;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientSession {
  @Getter
  private SessionState state;

  @JsonComponent
  public static class Deserializer extends JsonDeserializer<ClientSession> {

    @Override
    public ClientSession deserialize(
        JsonParser jsonParser,
        DeserializationContext deserializationContext
    ) throws IOException {
      JsonNode node = jsonParser.getCodec().readTree(jsonParser);
      SessionState myState;
      switch (node.get("status").asText()) {
        case "closed":
          myState = SessionState.CLOSED;
          break;
        case "open":
          myState = SessionState.OPEN;
          break;
        case "quarantined":
          myState = SessionState.QUARANTINED;
          break;
        default:
          throw new IOException("Could not parse JSON");
      }
      return ClientSession.builder()
          .state(myState)
          .build();
    }
  }

}
