package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.questions.Question;
import ch.heigvd.pro.b04.sessions.Session;
import java.io.Serializable;
import java.util.Objects;
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
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ServerPoll implements Serializable {

  @EmbeddedId
  @Getter
  private ServerPollIdentifier idPoll;

  @OneToMany(mappedBy = "idQuestion.idxPoll", cascade = CascadeType.ALL)
  private Set<Question> pollQuestions;

  @OneToMany(mappedBy = "idSession.idxPoll", cascade = CascadeType.ALL)
  private Set<Session> sessionSet;

  @Getter
  private String title;

  /**
   * Returns a boolean indicating whether a certain token has the permissions to perform some
   * changes on a given poll.
   *
   * @param poll       The {@link ServerPoll} for which we're checking the permissions.
   * @param token      The token for which we're checking permissions.
   * @param repository The repository in which moderators can be found.
   * @return True if the user may make modifications to the poll, false otherwise.
   */
  public static boolean isAvailableWithToken(
      ServerPoll poll,
      String token,
      ModeratorRepository repository) {
    return repository.findBySecret(token)
        .map(moderator -> Objects.equals(moderator, poll.idPoll.getIdxModerator()))
        .orElse(false);
  }

  /**
   * Add a new {@link Question} to this {@link ServerPoll} instance.
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
   * Add a new {@link Session} to this {@link ServerPoll} instance.
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
