package ch.heigvd.pro.b04.answers;

import ch.heigvd.pro.b04.votes.ServerVote;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServerAnswer {

  @EmbeddedId
  private ServerAnswerIdentifier idAnswer;

  private String title;
  private String description;

  @OneToMany(mappedBy = "idVote.idxServerAnswer", cascade = CascadeType.ALL)
  private Set<ServerVote> voteSet;

  @JsonComponent
  public static class Serializer extends JsonSerializer<ServerAnswer> {

    @Override
    public void serialize(
        ServerAnswer answer,
        JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider
    ) throws IOException {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeStringField("title", answer.getTitle());
      jsonGenerator.writeStringField("description", answer.getDescription());
      jsonGenerator.writeNumberField("idModerator", answer.getIdAnswer()
          .getIdxServerQuestion().getIdServerQuestion()
          .getIdxPoll().getIdPoll()
          .getIdxModerator().getIdModerator());
      jsonGenerator.writeNumberField("idPoll", answer.getIdAnswer()
          .getIdxServerQuestion().getIdServerQuestion()
          .getIdxPoll().getIdPoll().getIdPoll());
      jsonGenerator.writeNumberField("idQuestion", answer.getIdAnswer()
          .getIdxServerQuestion().getIdServerQuestion().getIdServerQuestion());
      jsonGenerator.writeNumberField("idAnswer", answer.getIdAnswer().getIdAnswer());
      jsonGenerator.writeEndObject();
    }
  }
}
