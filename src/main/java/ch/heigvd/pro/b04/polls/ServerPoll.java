package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.questions.Question;
import ch.heigvd.pro.b04.sessions.ServerSession;
import ch.heigvd.pro.b04.sessions.SessionIdentifier;
import ch.heigvd.pro.b04.sessions.SessionRepository;
import ch.heigvd.pro.b04.sessions.SessionState;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

  @OneToMany(mappedBy = "idQuestion.idxPoll", cascade = CascadeType.ALL)
  private Set<Question> pollQuestions;

  @OneToMany(mappedBy = "idSession.idxPoll", cascade = CascadeType.ALL)
  private Set<ServerSession> serverSessionSet;

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
   * Creates a new Session and inserts it in the database.
   * @param repository The repository containing the new Session
   */
  @Transactional
  public ServerSession newSession(SessionRepository repository) {
    // Make sure that we do not trigger the unique clause for a session code
    String sessionCode;
    do {
      sessionCode = ServerSession.createSessionCode();
    } while (repository.findByCode(sessionCode).isPresent());

    ServerSession newServerSession = ServerSession.builder()
        .idSession(SessionIdentifier.builder()
            .idSession(SessionIdentifier.getNewIdentifier(repository))
            .idxModerator(idPoll.getIdxModerator())
            .idxPoll(this)
            .build())
        .code(sessionCode)
        .state(SessionState.OPEN)
        .timestampStart(new Timestamp(System.currentTimeMillis()))
        .build();

    return repository.saveAndFlush(newServerSession);
  }

  /** Returns the latest session made in this poll.
   *
   * @param repository The session repository
   * @return An Optional of serversession. If set, it contains the last session made in this poll
   */
  public Optional<ServerSession> getLatestSession(SessionRepository repository) {
    List<ServerSession> allSessions =
        repository.findByModAndPoll(idPoll.getIdxModerator(), this);

    return allSessions.stream().max(Comparator.comparing(ServerSession::getTimestampStart));
  }
}
