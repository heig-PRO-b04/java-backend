package ch.heigvd.pro.b04.votes;

import ch.heigvd.pro.b04.answers.ServerAnswer;
import ch.heigvd.pro.b04.participants.Participant;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class VoteIdentifier implements Serializable {

  @Column
  private final Timestamp timeVote = new Timestamp(System.currentTimeMillis());

  @ManyToOne
  @PrimaryKeyJoinColumn
  private Participant idxParticipant;

  @ManyToOne
  @PrimaryKeyJoinColumn
  private ServerAnswer idxServerAnswer;
}
