package ch.heigvd.pro.b04.questions;

public enum QuestionVisibility {
  VISIBLE("visible"), HIDDEN("hidden"), ARCHIVED("archived");

  private final String representation;

  /* private */ QuestionVisibility(String representation) {
    this.representation = representation;
  }

  public String getRepresentation() {
    return representation;
  }
}
