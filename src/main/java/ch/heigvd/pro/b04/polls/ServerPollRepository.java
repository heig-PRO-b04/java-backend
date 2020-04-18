package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.moderators.Moderator;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ServerPollRepository extends JpaRepository<ServerPoll, ServerPollIdentifier> {

  @Query("SELECT p FROM ServerPoll p WHERE p.idPoll.idxModerator = :moderator")
  List<ServerPoll> findAllByModerator(Moderator moderator);

  @Query("SELECT p FROM ServerPoll p"
      + " WHERE p.idPoll.idxModerator = :moderator AND p.idPoll.idPoll = :pollId")
  List<ServerPoll> findByModeratorAndId(Moderator moderator, long pollId);

  @Transactional
  @Modifying
  @Query("UPDATE ServerPoll p SET p.title = :title WHERE p.idPoll = :id")
  int update(ServerPollIdentifier id, String title);
}
