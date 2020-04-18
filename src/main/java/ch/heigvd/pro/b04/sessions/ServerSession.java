package ch.heigvd.pro.b04.sessions;

import ch.heigvd.pro.b04.participants.Participant;
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


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode
public class Session {
  private static final int CODE_BOUNDARY_EXCLUDED = 0x10000;

  public enum State {
    OPEN, CLOSED_TO_NEW_ONES, CLOSED
  }

  /**
   * Creates a new sessionCode in hexadecimal format.
   * @return A String containing the randomly generated session code
   */
  public static String createSessionCode() {
    Integer rand = new Random().nextInt(CODE_BOUNDARY_EXCLUDED);
    return "0x" + Integer.toHexString(rand);
  }

  /**
   * Creates a new unique identifier for a Session.
   * @param repository The repository to which we will add a new Session to
   * @return A new unique identifier
   */
  public static Long getNewIdentifier(SessionRepository repository) {
    Long identifier = repository.findAll().stream()
        .map(Session::getIdSession)
        .map(SessionIdentifier::getIdSession)
        .max(Long::compareTo)
        .map(id -> id + 1)
        .orElse(1L);
    return identifier;
  }

  @Getter
  @EmbeddedId
  private SessionIdentifier idSession;

  @Exclude
  @OneToMany(mappedBy = "idParticipant.idxSession", cascade = CascadeType.ALL)
  private Set<Participant> participantSet;

  @Getter
  private Timestamp timestampStart;
  @Getter
  private Timestamp timestampEnd;
  @Getter
  @Setter
  @Column(unique = true)
  private String code;
  @Setter
  @Getter
  private State state;

  /**
   * Constructor of a new {@link Session}.
   *
   * @param id PK to assign to this Session
   */
  public Session(long id) {
    idSession = new SessionIdentifier(id);
    state = State.CLOSED;
    code = createSessionCode();
  }
}