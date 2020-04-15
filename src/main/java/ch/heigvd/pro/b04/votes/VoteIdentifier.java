package ch.heigvd.pro.b04.votes;

import ch.heigvd.pro.b04.answers.Answer;
import ch.heigvd.pro.b04.participants.Participant;
import ch.heigvd.pro.b04.participants.ParticipantIdentifier;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Embeddable
public class VoteIdentifier implements Serializable {

  @Column
  private Timestamp timeVote;

  @ManyToOne
  @PrimaryKeyJoinColumn
  private Participant idxParticipant;

  @ManyToOne
  @PrimaryKeyJoinColumn
  private Answer idxAnswer;

  public VoteIdentifier(Participant nP, Answer nA)
  {
    timeVote=new Timestamp(System.currentTimeMillis());
    this.idxParticipant=nP;
    this.idxAnswer=nA;
  }

}
