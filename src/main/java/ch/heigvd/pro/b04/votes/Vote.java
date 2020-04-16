package ch.heigvd.pro.b04.votes;

import ch.heigvd.pro.b04.answers.Answer;
import ch.heigvd.pro.b04.participants.Participant;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Vote {

  @Getter
  @EmbeddedId
  private VoteIdentifier idVote;

  @Getter
  @Setter
  private boolean answerChecked;

  public Vote(Participant participant, Answer answer) {
    idVote = new VoteIdentifier(participant, answer);
    answerChecked = false;
  }
}
