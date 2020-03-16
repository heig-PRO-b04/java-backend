package ch.heigvd.pro.b04.polls;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import java.io.Serializable;

@Data
@Embeddable
public class PollIdentifier implements Serializable {
  private String idxModerator;
  @GeneratedValue private Long idPoll;

  public PollIdentifier() {}

  public PollIdentifier(String moderatorName) {
      this.idxModerator = moderatorName;
  }
}
