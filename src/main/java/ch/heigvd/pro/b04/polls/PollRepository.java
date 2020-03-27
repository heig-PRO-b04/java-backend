package ch.heigvd.pro.b04.polls;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PollRepository extends JpaRepository<Poll, PollIdentifier>,
    JpaSpecificationExecutor<Poll> {

}
