package ch.heigvd.pro.b04.answers;

import ch.heigvd.pro.b04.questions.ServerQuestion;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import lombok.Data;

@Data
@Embeddable
public class AnswerIdentifier implements Serializable {

  @Column
  private long idAnswer;

  @ManyToOne
  @PrimaryKeyJoinColumn
  private ServerQuestion idxServerQuestion;

  public AnswerIdentifier() {
  }

  public AnswerIdentifier(long id) {
    this.idAnswer = id;
  }
}