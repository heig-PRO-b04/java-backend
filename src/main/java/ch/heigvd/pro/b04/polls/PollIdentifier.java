package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.moderators.*;
import javax.persistence.ManyToOne;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import java.io.Serializable;

@Data
@Embeddable
public class PollIdentifier implements Serializable {
  private String idxModerator;
  //@GeneratedValue
  private Long idPoll;

  public PollIdentifier() {}

  public PollIdentifier(String moderatorName) {
      this.idxModerator = moderatorName;
      idPoll=new Long(1);
  }
}
