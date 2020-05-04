package ch.heigvd.pro.b04.votes;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerVoteRepository extends JpaRepository<ServerVote, VoteIdentifier> {
}
