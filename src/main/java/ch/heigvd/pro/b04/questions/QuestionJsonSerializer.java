package ch.heigvd.pro.b04.questions;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class QuestionJsonSerializer extends JsonSerializer<Question> {

  @Override
  public void serialize(
      Question question,
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider
  ) throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeStringField("idModerator",
        question.getIdQuestion().getIdxPoll().getIdPoll().getIdxModerator().getIdModerator());
    jsonGenerator
        .writeNumberField("idPoll", question.getIdQuestion().getIdxPoll().getIdPoll().getIdPoll());
    jsonGenerator.writeNumberField("idQuestion", question.getIdQuestion().getIdQuestion());
    jsonGenerator.writeEndObject();
  }
}
