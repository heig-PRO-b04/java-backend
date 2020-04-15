package ch.heigvd.pro.b04.votes;

import ch.heigvd.pro.b04.answers.Answer;
import ch.heigvd.pro.b04.participants.Participant;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Vote {

  @Getter
  @EmbeddedId
  private VoteIdentifier idVote;

  @Getter
  @Setter
  private boolean answerChecked;

  public Vote(Answer answer, Participant participant) {
    idVote = new VoteIdentifier(participant, answer);
    answerChecked = false;
  }
}
