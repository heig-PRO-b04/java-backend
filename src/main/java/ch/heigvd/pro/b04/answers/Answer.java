package ch.heigvd.pro.b04.answers;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Answer {

  @EmbeddedId
  private AnswerIdentifier idAnswer;

  private String text;
  private String description;

  public Answer(long id, String title, String description) {
    this.idAnswer = new AnswerIdentifier(id);
    this.text = title;
    this.description = description;
  }
}
