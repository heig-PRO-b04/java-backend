package ch.heigvd.pro.b04.polls;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class PollIdentifier implements Serializable {

  private Long idModerator;
  private Long idPoll;

  public PollIdentifier() {
  }

  public PollIdentifier(Long moderator, Long poll) {
    this.idModerator = moderator;
    this.idPoll = poll;
  }
}
