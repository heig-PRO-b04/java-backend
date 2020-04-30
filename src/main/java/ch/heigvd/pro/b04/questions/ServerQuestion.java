package ch.heigvd.pro.b04.questions;

import ch.heigvd.pro.b04.answers.Answer;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.websocket.server.ServerEndpoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor
public class ServerQuestion {
  @Setter
  @Getter
  protected double indexInPoll;
  @Setter
  @Getter
  protected String title;
  @Setter
  @Getter
  protected String details;
  @Setter
  @Getter
  protected QuestionVisibility visibility;
  @Setter
  @Getter
  protected short answersMin;
  @Setter
  @Getter
  protected short answersMax;
  @Getter
  @EmbeddedId
  private ServerQuestionIdentifier idServerQuestion;
  @Getter
  @OneToMany(mappedBy = "idAnswer.idxServerQuestion", cascade = CascadeType.ALL)
  private Set<Answer> answersToQuestion;

  /**
   * Add a new answer to this {@link ServerQuestion}.
   *
   * @param newAnswer The {@link Answer} to add.
   */
  public void addAnswer(Answer newAnswer) {
    newAnswer.getIdAnswer().setIdxServerQuestion(this);
    if (answersToQuestion == null) {
      answersToQuestion = Stream.of(newAnswer).collect(Collectors.toSet());
    } else {
      answersToQuestion.add(newAnswer);
    }
  }
}
