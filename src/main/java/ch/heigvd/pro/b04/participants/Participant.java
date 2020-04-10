package ch.heigvd.pro.b04.participants;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Participant {
  @EmbeddedId
  private ParticipantIdentifier idParticipant;

  private String username;

  public Participant(long id, String username)
  {
    idParticipant=new ParticipantIdentifier(id);
    this.username=username;
  }
}
