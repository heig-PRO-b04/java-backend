package ch.heigvd.pro.b04.votes;

import ch.heigvd.pro.b04.answers.ServerAnswer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerVoteRepository extends JpaRepository<ServerVote, VoteIdentifier> {

  List<ServerVote> findAllByIdVote_IdxServerAnswer(ServerAnswer answer);
}
