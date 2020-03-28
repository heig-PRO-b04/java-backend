package ch.heigvd.pro.b04.polls;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class PollJsonSerializer extends JsonSerializer<Poll> {

  @Override
  public void serialize(
      Poll poll,
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider
  ) throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeStringField("idModerator", poll.getIdPoll().getIdxModerator().getIdModerator());
    jsonGenerator.writeNumberField("idPoll", poll.getIdPoll().getIdPoll());
    jsonGenerator.writeStringField("title", poll.getTitle());
    jsonGenerator.writeEndObject();
  }
}
