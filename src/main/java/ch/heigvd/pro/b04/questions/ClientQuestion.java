package ch.heigvd.pro.b04.questions;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.jackson.JsonComponent;

@Builder
public class ClientQuestion {

  @Getter
  protected double indexInPoll;

  @Getter
  protected String title;

  @Getter
  protected String details;

  @Getter
  protected QuestionVisibility visibility;

  @Getter
  protected short answersMin;

  @Getter
  protected short answersMax;

  @JsonComponent
  public static class Deserializer extends JsonDeserializer<ClientQuestion> {

    @Override
    public ClientQuestion deserialize(
        JsonParser jsonParser,
        DeserializationContext deserializationContext
    ) throws IOException {
      try {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        // TODO : Factorize this in the QuestionVisibility enum.
        QuestionVisibility visibility;
        switch (node.get("visibility").asText()) {
          case "visible":
            visibility = QuestionVisibility.VISIBLE;
            break;
          case "archived":
            visibility = QuestionVisibility.ARCHIVED;
            break;
          case "hidden":
          default:
            visibility = QuestionVisibility.HIDDEN;
            break;
        }
        return ClientQuestion.builder()
            .title(node.get("title").asText())
            .details(node.get("details").asText())
            .visibility(visibility)
            .answersMin((short) node.get("answersMin").asInt())
            .answersMax((short) node.get("answersMax").asInt())
            .build();
      } catch (Exception anything) {
        anything.printStackTrace();
        throw anything;
      }
    }
  }
}
