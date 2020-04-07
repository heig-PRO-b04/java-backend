package ch.heigvd.pro.b04.questions;

import ch.heigvd.pro.b04.answers.Answer;
import ch.heigvd.pro.b04.utils.Constants.QuestionVisbility;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Question {

  @EmbeddedId
  private QuestionIdentifier idQuestion;

  @OneToMany(mappedBy = "idAnswer.idxQuestion", cascade = CascadeType.ALL)
  private Set<Answer> answersToQuestion;

  private double indexInPoll;
  private String title;
  private String details;
  private QuestionVisbility visibility;
  private short answersMin;
  private short answersMax;

  public Question() {
  }

  /**
   * Creates a new {@link Question}.
   *
   * @param id      The question identifier.
   * @param index   The question index.
   * @param title   The question title.
   * @param details The question details.
   * @param visible The question visibility.
   * @param min     The lower bound for the number of required answers.
   * @param max     The upper bound for the number of required answers.
   */
  public Question(long id,
      double index,
      String title,
      String details,
      QuestionVisbility visible,
      short min,
      short max) {
    this.idQuestion = new QuestionIdentifier(id);
    this.indexInPoll = index;
    this.title = title;
    this.details = details;
    visibility = visible;
    this.answersMax = max;
    this.answersMin = min;
  }

  public QuestionIdentifier getIdQuestion() {
    return idQuestion;
  }

  /**
   * Add a new answer to this {@link Question}.
   *
   * @param newAnswer The {@link Answer} to add.
   */
  public void addAnswer(Answer newAnswer) {
    newAnswer.getIdAnswer().setIdxQuestion(this);
    if (answersToQuestion == null) {
      answersToQuestion = Stream.of(newAnswer).collect(Collectors.toSet());
    } else {
      answersToQuestion.add(newAnswer);
    }
  }
}
