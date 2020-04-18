package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.questions.ClientQuestion;
import ch.heigvd.pro.b04.questions.QuestionRepository;
import ch.heigvd.pro.b04.questions.ServerQuestion;
import ch.heigvd.pro.b04.questions.ServerQuestionIdentifier;
import ch.heigvd.pro.b04.sessions.Session;
import ch.heigvd.pro.b04.sessions.Session.State;
import ch.heigvd.pro.b04.sessions.SessionIdentifier;
import ch.heigvd.pro.b04.sessions.SessionRepository;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.transaction.Transactional;
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

  @Getter
  @OneToMany(mappedBy = "idServerQuestion.idxPoll", cascade = CascadeType.ALL)
  private Set<ServerQuestion> pollServerQuestions;

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
    return repository.findByToken(token)
        .map(moderator -> Objects.equals(moderator, poll.idPoll.getIdxModerator()))
        .orElse(false);
  }

  /**
   * Creates a new unique identifier for a {@link ServerQuestion}.
   *
   * @param repository The repository to which we will add a new Session to
   * @return A new unique identifier
   */
  public static Long getNewIdentifier(QuestionRepository repository) {
    Long identifier = repository.findAll().stream()
        .map(ServerQuestion::getIdServerQuestion)
        .map(ServerQuestionIdentifier::getIdServerQuestion)
        .max(Long::compareTo)
        .map(id -> id + 1)
        .orElse(1L);
    return identifier;
  }

  /**
   * Add a new {@link ServerQuestion} to this {@link ServerPoll} instance.
   *
   * @param newQuestion The question to be added.
   */
  @Transactional
  public ServerQuestion newQuestion(QuestionRepository repoQ, ClientQuestion newQuestion) {
    ServerQuestion qqW=ServerQuestion.builder()
        .id(getNewIdentifier(repoQ))
        .title(newQuestion.getTitle())
        .details(newQuestion.getDetails())
        .visible(newQuestion.getVisibility())
        .index(newQuestion.getIndexInPoll())
        .max(newQuestion.getAnswersMax())
        .min(newQuestion.getAnswersMin()).build();
    return repoQ.save(qqW);
  }


  /**
   * Creates a new Session and inserts it in the database.
   *
   * @param repository The repository containing the new Session
   */
  @Transactional
  public Session newSession(SessionRepository repository) {
    Long identifier = Session.getNewIdentifier(repository);
    String sessionCode;

    // Make sure that we do not trigger the unique clause for a session code
    do {
      sessionCode = Session.createSessionCode();
    } while (repository.findByCode(sessionCode).isPresent());

    Session newSession = Session.builder()
        .idSession(new SessionIdentifier(identifier))
        .code(sessionCode)
        .state(State.CLOSED)
        .build();

    return repository.saveAndFlush(newSession);
  }
}
