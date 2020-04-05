package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.moderators.Moderator;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PollRepository extends JpaRepository<Poll, PollIdentifier> {

  @Query("SELECT p FROM Poll p WHERE p.idPoll.idxModerator = :moderator")
  List<Poll> findAllByModerator(Moderator moderator);
}
