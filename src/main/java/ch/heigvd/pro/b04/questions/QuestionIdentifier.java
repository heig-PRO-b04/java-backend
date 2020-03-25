package ch.heigvd.pro.b04.questions;

import ch.heigvd.pro.b04.polls.Poll;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Embeddable
public class QuestionIdentifier implements Serializable {

  @Column
  private long idQuestion;

  @ManyToOne
  @PrimaryKeyJoinColumn
  private Poll idxPoll;

  public QuestionIdentifier() {
  }

  public QuestionIdentifier(long id) {
    this.idQuestion = id;
  }

  public Poll getIdxPoll() {
    return idxPoll;
  }
}
