package ch.heigvd.pro.b04.polls;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PollRepository extends JpaRepository<Poll, PollIdentifier>,
    JpaSpecificationExecutor<Poll> {

}
