package ch.heigvd.pro.b04.answers;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Answer {

  @EmbeddedId
  private AnswerIdentifier idAnswer;

  private String text;
  private String description;

  public Answer() {
  }

  public Answer(long id, String title, String description) {
    idAnswer = new AnswerIdentifier(id);
    this.text = title;
  }
}
