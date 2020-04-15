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
public class ServerPollIdentifier implements Serializable {

  @Column
  private long idPoll;

  @ManyToOne
  @PrimaryKeyJoinColumn
  private Moderator idxModerator;

  public ServerPollIdentifier() {
  }

  public ServerPollIdentifier(long id) {
    this.idPoll = id;
  }
}
