package ch.heigvd.pro.b04.sessions;

import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.polls.ServerPoll;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SessionRepository extends JpaRepository<ServerSession, SessionIdentifier> {
  Optional<ServerSession> findByCode(String code);

  @Query("SELECT s FROM ServerSession s"
      + " WHERE s.idSession.idxPoll.idPoll.idxModerator = :moderator AND s.idSession.idxPoll = :poll")
  List<ServerSession> findByModAndPoll(Moderator moderator, ServerPoll poll);
}