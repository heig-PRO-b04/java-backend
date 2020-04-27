package ch.heigvd.pro.b04.votes;

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
}
