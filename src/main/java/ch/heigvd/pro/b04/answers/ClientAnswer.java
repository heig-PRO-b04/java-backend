package ch.heigvd.pro.b04.answers;

import ch.heigvd.pro.b04.questions.ClientQuestion;
import ch.heigvd.pro.b04.questions.QuestionVisibility;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import org.springframework.boot.jackson.JsonComponent;

@Builder
public class ClientAnswer {
  @Getter
  private String text;
  @Getter
  private String description;

  @JsonComponent
  public static class Deserializer extends JsonDeserializer<ClientAnswer> {

    @Override
    public ClientAnswer deserialize(
        JsonParser jsonParser,
        DeserializationContext deserializationContext
    ) throws IOException {
      try {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        return ClientAnswer.builder()
            .text(node.get("title").asText())
            .description(node.get("description").asText())
            .build();
      } catch (Exception anything) {
        anything.printStackTrace();
        throw anything;
      }
    }
  }
}
