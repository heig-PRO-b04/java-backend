package ch.heigvd.pro.b04.sessions;

import ch.heigvd.pro.b04.Constants;
import ch.heigvd.pro.b04.Constants.SessionState;
import ch.heigvd.pro.b04.participants.Participant;
import java.sql.Timestamp;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
public class Session {

  @Getter
  @EmbeddedId
  private SessionIdentifier idSession;

  @OneToMany(mappedBy = "idParticipant.idxSession", cascade = CascadeType.ALL)
  private Set<Participant> participantSet;

  @Getter
  @Setter
  private Timestamp timestampStart;
  @Getter
  @Setter
  private Timestamp timestampEnd;
  @Getter
  @Column(unique = true)
  private String code;
  @Getter
  @Setter
  private Constants.SessionState state;

  /**
   * Constructor of a new {@link Session}.
   *
   * @param id PK to assign to this Session
   */
  public Session(long id) {
    idSession = new SessionIdentifier(id);
    state = SessionState.OPEN; //TODO: set to closed by default
    code = Integer.toHexString(new Random().nextInt(0xFFFf));
  }

  /**
   * Add a new {@link Participant} to current Session.
   *
   * @param newP participant to add
   */
  public void addParticipant(Participant newP) {
    newP.getIdParticipant().setIdxSession(this);
    if (participantSet == null) {
      participantSet = Stream.of(newP).collect(Collectors.toSet());
    } else {
      participantSet.add(newP);
    }
  }
}