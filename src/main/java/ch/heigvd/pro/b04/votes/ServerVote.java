package ch.heigvd.pro.b04.votes;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ServerVote {

  @Getter
  @EmbeddedId
  private VoteIdentifier idVote;

  @Getter
  private boolean answerChecked;

  @JsonComponent
  public static class Serializer extends JsonSerializer<ServerVote> {

    @Override
    public void serialize(
        ServerVote vote,
        JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider
    ) throws IOException {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeBooleanField("checked", vote.isAnswerChecked());
      jsonGenerator.writeNumberField("idModerator", vote.getIdVote()
          .getIdxServerAnswer().getIdAnswer().getIdxServerQuestion()
          .getIdServerQuestion().getIdxPoll().getIdPoll().getIdxModerator().getIdModerator());
      jsonGenerator.writeNumberField("idPoll", vote.getIdVote()
          .getIdxServerAnswer().getIdAnswer().getIdxServerQuestion()
          .getIdServerQuestion().getIdxPoll().getIdPoll().getIdPoll());
      jsonGenerator.writeNumberField("idQuestion", vote.getIdVote()
          .getIdxServerAnswer().getIdAnswer().getIdxServerQuestion()
          .getIdServerQuestion().getIdServerQuestion());
      jsonGenerator.writeNumberField("idAnswer", vote.getIdVote()
          .getIdxServerAnswer().getIdAnswer().getIdAnswer());
      jsonGenerator.writeEndObject();
    }
  }
}
