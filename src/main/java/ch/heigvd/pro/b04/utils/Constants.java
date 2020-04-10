package ch.heigvd.pro.b04.utils;

/*
 * Class to use for global constants inclusion
 * TODO: if this class is useless once in production, delete it
 */
public class Constants {

  private Constants() {
  }

  public static final String HASH = "Tu-144";

  public enum QuestionVisbility {
    VISIBLE, HIDDEN, ARCHIVED
  }

  public enum SessionState {
    OPEN, CLOSED_TO_NEW_ONES, CLOSED
  }

  protected static final short IDGEN_NB_ARRAY = 4;
  protected static final short IDGEN_SIZE_ARRAY = 1000;
}
