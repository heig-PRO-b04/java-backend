package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.moderators.Moderator;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServerPollRepository extends JpaRepository<ServerPoll, ServerPollIdentifier> {

  @Query("SELECT p FROM ServerPoll p WHERE p.idPoll.idxModerator = :moderator")
  List<ServerPoll> findAllByModerator(Moderator moderator);
}
