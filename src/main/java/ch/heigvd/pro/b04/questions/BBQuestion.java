package ch.heigvd.pro.b04.questions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public abstract class BBQuestion {
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
