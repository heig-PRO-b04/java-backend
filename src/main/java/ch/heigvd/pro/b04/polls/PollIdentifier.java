package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.moderators.Moderator;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import lombok.Data;

@Data
@Embeddable
public class PollIdentifier implements Serializable {

  @Column
  private Long idPoll;

  @ManyToOne
  @PrimaryKeyJoinColumn
  private Moderator idxModerator;

  public PollIdentifier() {
  }

  public PollIdentifier(long id) {
    this.idPoll = id;
  }
}
