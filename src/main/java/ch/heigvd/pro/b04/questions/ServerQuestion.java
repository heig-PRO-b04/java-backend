package ch.heigvd.pro.b04.questions;

import ch.heigvd.pro.b04.answers.Answer;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Entity
public class ServerQuestion extends Question {

  @Getter
  @EmbeddedId
  private ServerQuestionIdentifier idServerQuestion;

  @OneToMany(mappedBy = "idAnswer.idxServerQuestion", cascade = CascadeType.ALL)
  private Set<Answer> answersToQuestion;

  public ServerQuestion() {
  }

  /**
   * Creates a new {@link ServerQuestion}.
   *
   * @param identifier The question identifier.
   * @param index      The question index.
   * @param title      The question title.
   * @param details    The question details.
   * @param visible    The question visibility.
   * @param min        The lower bound for the number of required answers.
   * @param max        The upper bound for the number of required answers.
   */
  @Builder
  public ServerQuestion(
      ServerQuestionIdentifier identifier,
      double index,
      String title,
      String details,
      QuestionVisibility visible,
      short min,
      short max) {
    this.idServerQuestion = identifier;
    this.indexInPoll = index;
    this.title = title;
    this.details = details;
    this.visibility = visible;
    this.answersMax = max;
    this.answersMin = min;
  }

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
