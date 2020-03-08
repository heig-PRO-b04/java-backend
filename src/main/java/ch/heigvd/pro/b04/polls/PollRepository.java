package ch.heigvd.pro.b04.polls;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, PollIdentifier> {}
