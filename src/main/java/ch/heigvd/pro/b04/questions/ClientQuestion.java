package ch.heigvd.pro.b04.questions;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import lombok.Builder;
import org.springframework.boot.jackson.JsonComponent;

public class ClientQuestion extends Question {

  /**
   * Standard constructor, necessary to use @builder with inheritance.
   *
   * @param indexInPoll The question index.
   * @param title       The question title.
   * @param details     The question details.
   * @param visibility  The question visibility.
   * @param answersMin  The lower bound for the number of required answers.
   * @param answersMax  The upper bound for the number of required answers.
   */
  @Builder
  public ClientQuestion(
      double indexInPoll,
      String title,
      String details,
      QuestionVisibility visibility,
      short answersMin,
      short answersMax) {
    this.indexInPoll = indexInPoll;
    this.title = title;
    this.details = details;
    this.visibility = visibility;
    this.answersMin = answersMin;
    this.answersMax = answersMax;
  }

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
