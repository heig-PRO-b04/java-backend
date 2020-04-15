package ch.heigvd.pro.b04.questions;

import ch.heigvd.pro.b04.polls.ServerPoll;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import lombok.Data;

@Data
@Embeddable
public class QuestionIdentifier implements Serializable {

  @Column
  private long idQuestion;

  @ManyToOne
  @PrimaryKeyJoinColumn
  private ServerPoll idxPoll;

  public QuestionIdentifier() {
  }

  public QuestionIdentifier(long id) {
    this.idQuestion = id;
  }

  public ServerPoll getIdxPoll() {
    return idxPoll;
  }
}
