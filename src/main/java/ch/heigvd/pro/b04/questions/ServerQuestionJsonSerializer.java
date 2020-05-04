package ch.heigvd.pro.b04.questions;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class ServerQuestionJsonSerializer extends JsonSerializer<ServerQuestion> {

  @Override
  public void serialize(
      ServerQuestion question,
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider
  ) throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeNumberField("idModerator",
        question.getIdServerQuestion().getIdxPoll()
            .getIdPoll().getIdxModerator()
            .getIdModerator()
    );
    jsonGenerator.writeNumberField("idPoll",
        question.getIdServerQuestion().getIdxPoll()
            .getIdPoll().getIdPoll()
    );
    jsonGenerator.writeNumberField("idQuestion",
        question.getIdServerQuestion().getIdServerQuestion());
    jsonGenerator.writeNumberField("indexInPoll", question.getIndexInPoll());
    jsonGenerator.writeStringField("title", question.getTitle());
    jsonGenerator.writeStringField("details", question.getDetails());
    jsonGenerator.writeNumberField("answerMin", question.getAnswersMin());
    jsonGenerator.writeNumberField("answerMax", question.getAnswersMax());
    jsonGenerator.writeStringField("visibility", question.getVisibility().getRepresentation());
    jsonGenerator.writeEndObject();
  }
}
