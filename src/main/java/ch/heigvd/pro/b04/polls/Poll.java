package ch.heigvd.pro.b04.polls;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Poll {

  @EmbeddedId
  private PollIdentifier id;
  private String title;

  public Poll() {
  }

  public Poll(String title) {
    this.title = title;
  }
}
