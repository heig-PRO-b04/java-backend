package ch.heigvd.pro.b04.votes;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.jackson.JsonComponent;

@Builder
public class BooleanVote {

  @Getter
  private boolean checked;

  @JsonComponent
  public static class Deserializer extends JsonDeserializer<BooleanVote> {

    @Override
    public BooleanVote deserialize(
        JsonParser jsonParser,
        DeserializationContext deserializationContext
    ) throws IOException {
      JsonNode node = jsonParser.getCodec().readTree(jsonParser);
      return BooleanVote.builder()
          .checked(node.get("checked").asBoolean())
          .build();
    }
  }
}
