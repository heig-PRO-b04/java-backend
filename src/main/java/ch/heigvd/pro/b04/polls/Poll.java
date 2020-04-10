package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.questions.Question;
import java.io.Serializable;
import java.util.Objects;
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

  private String title;

  public Poll() {
  }

  public Poll(long id, String title) {
    idPoll = new PollIdentifier(id);
    this.title = title;
  }

  /**
   * Returns a boolean indicating whether a certain token has the permissions to perform some
   * changes on a given poll.
   *
   * @param poll       The {@link Poll} for which we're checking the permissions.
   * @param token      The token for which we're checking permissions.
   * @param repository The repository in which moderators can be found.
   * @return True if the user may make modifications to the poll, false otherwise.
   */
  public static boolean isAvailableWithToken(
      Poll poll,
      String token,
      ModeratorRepository repository) {
    return repository.findBySecret(token)
        .map(moderator -> Objects.equals(moderator, poll.idPoll.getIdxModerator()))
        .orElse(false);
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
}
