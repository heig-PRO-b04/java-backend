package ch.heigvd.pro.b04.polls;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Data
@Entity
public class Poll {
  @EmbeddedId private PollIdentifier id;
  private String title;

  public Poll() {}

  public Poll(String title) {
    this.title = title;
  }
}
