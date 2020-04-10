package ch.heigvd.pro.b04.sessions;

import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.polls.Poll;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class SessionIdentifier implements Serializable {
    @Column
  private long idSession;

    @ManyToOne
  @PrimaryKeyJoinColumn
  private Moderator idxModerator;

  @ManyToOne
  @PrimaryKeyJoinColumn
  private Poll idxPoll;

  public SessionIdentifier(long id)
  {
    this.idSession=id;
  }
}
