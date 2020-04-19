package ch.heigvd.pro.b04.sessions;

import ch.heigvd.pro.b04.participants.Participant;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Random;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Exclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.jackson.JsonComponent;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode
public class ServerSession {
  private static final int CODE_BOUNDARY_EXCLUDED = 0x10000;

  /**
   * Creates a new sessionCode in hexadecimal format.
   * @return A String containing the randomly generated session code
   */
  public static String createSessionCode() {
    int rand = new Random().nextInt(CODE_BOUNDARY_EXCLUDED);
    return ("0x" + Integer.toHexString(rand).toUpperCase());
  }

  @Getter
  @EmbeddedId
  private SessionIdentifier idSession;

  @Exclude
  @OneToMany(mappedBy = "idParticipant.idxServerSession", cascade = CascadeType.ALL)
  private Set<Participant> participantSet;

  @Getter
  @Setter
  private Timestamp timestampStart;
  @Getter
  @Setter
  private Timestamp timestampEnd;
  @Getter
  @Setter
  @Column(unique = true)
  private String code;
  @Setter
  @Getter
  private SessionState state;

  /**
   * Closes the session and sets the end timestamp.
   *
   * <p>Note: this doesn't save to database</p>
   */
  public void close() {
    setState(SessionState.CLOSED);
    setTimestampEnd(new Timestamp(System.currentTimeMillis()));
  }

  @JsonComponent
  public static class Serializer extends JsonSerializer<ServerSession> {

    @Override
    public void serialize(
        ServerSession session,
        JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider
    ) throws IOException {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeNumberField("idModerator",
          session.idSession.getIdxModerator().getIdModerator());
      jsonGenerator.writeNumberField("idPoll",
          session.idSession.getIdxPoll().getIdPoll().getIdPoll());
      jsonGenerator.writeNumberField("idSession",
          session.idSession.getIdSession());
      jsonGenerator.writeStringField("code", session.code);
      jsonGenerator.writeStringField("status", session.state.toString().toLowerCase());
      jsonGenerator.writeEndObject();
    }
  }
}