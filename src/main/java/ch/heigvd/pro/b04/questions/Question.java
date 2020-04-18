package ch.heigvd.pro.b04.questions;

import lombok.Getter;
import lombok.Setter;

public abstract class Question {
  @Getter
  @Setter
  protected double indexInPoll;
  @Getter
  @Setter
  protected String title;
  @Getter
  @Setter
  protected String details;
  @Getter
  @Setter
  protected QuestionVisibility visibility;
  @Getter
  @Setter
  protected short answersMin;
  @Getter
  @Setter
  protected short answersMax;
}
