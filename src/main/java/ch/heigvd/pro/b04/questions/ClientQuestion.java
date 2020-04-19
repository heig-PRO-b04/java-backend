package ch.heigvd.pro.b04.questions;

import lombok.Builder;

public class ClientQuestion extends Question {

  /**
   * Standard constructor, necessary to use @builder with inheritance.
   *
   * @param indexInPoll   The question index.
   * @param title   The question title.
   * @param details The question details.
   * @param visibility The question visibility.
   * @param answersMin     The lower bound for the number of required answers.
   * @param answersMax     The upper bound for the number of required answers.
   */
  @Builder
  public ClientQuestion(double indexInPoll, String title, String details,
      QuestionVisibility visibility, short answersMin, short answersMax) {
    this.indexInPoll = indexInPoll;
    this.title = title;
    this.details = details;
    this.visibility = visibility;
    this.answersMin = answersMin;
    this.answersMax = answersMax;
  }
}
