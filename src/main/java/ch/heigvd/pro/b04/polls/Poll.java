package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.questions.Question;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.Data;


@Data
@Entity
public class Poll implements Serializable {

  @EmbeddedId
  private PollIdentifier idPoll;

  @OneToMany(mappedBy = "idQuestion.idxPoll", cascade = CascadeType.ALL)
  private Set<Question> pollQuestions;

  @OneToMany(mappedBy = "idSession.idxPoll", cascade = CascadeType.ALL)
  private Set<Session> sessionSet;

  private String title;

  public Poll() {
  }

  public Poll(long id, String title) {
    idPoll = new PollIdentifier(id);
    this.title = title;
  }

  public PollIdentifier getIdPoll() {
    return idPoll;
  }

  /**
   * Add a new {@link Question} to this {@link Poll} instance.
   *
   * @param newQuestion The question to be added.
   */
  public void addQuestion(Question newQuestion) {
    newQuestion.getIdQuestion().setIdxPoll(this);
    if (pollQuestions == null) {
      pollQuestions = Stream.of(newQuestion).collect(Collectors.toSet());
    } else {
      pollQuestions.add(newQuestion);
    }
  }

  /**
   * Add a new {@link Session} to this {@link Poll} instance.
   *
   * @param newSession session to add
   */
  public void addSession(Session newSession) {
    newSession.getIdSession().setIdxPoll(this);
    if (sessionSet == null) {
      sessionSet = Stream.of(newSession).collect(Collectors.toSet());
    } else {
      sessionSet.add(newSession);
    }
  }
}
