package ch.heigvd.pro.b04.participants;

import ch.heigvd.pro.b04.sessions.Session;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@Builder
public class ParticipantIdentifier implements Serializable {

  @Column
  @Getter
  private long idParticipant;

  @Setter
  @ManyToOne
  @PrimaryKeyJoinColumn
  private Session idxSession;

  public ParticipantIdentifier(long id) {
    idParticipant = id;
  }
}