package ch.heigvd.pro.b04.sessions;

import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.polls.ServerPoll;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionIdentifier implements Serializable {

  /**
   * Creates a new unique identifier for a Session.
   * @param repository The repository to which we will add a new Session to
   * @return A new unique identifier
   */
  public static Long getNewIdentifier(SessionRepository repository) {
    Long identifier = repository.findAll().stream()
        .map(ServerSession::getIdSession)
        .map(SessionIdentifier::getIdSession)
        .max(Long::compareTo)
        .map(id -> id + 1)
        .orElse(1L);
    return identifier;
  }

  @Getter
  @Column
  private long idSession;

  @Getter
  @ManyToOne
  @PrimaryKeyJoinColumn
  private Moderator idxModerator;

  @Getter
  @Setter
  @ManyToOne
  @PrimaryKeyJoinColumn
  private ServerPoll idxPoll;
}