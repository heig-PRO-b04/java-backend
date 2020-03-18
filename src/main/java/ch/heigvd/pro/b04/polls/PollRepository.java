package ch.heigvd.pro.b04.polls;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource
public interface PollRepository extends JpaRepository<Poll, PollIdentifier>, JpaSpecificationExecutor<Poll> {
  //List<Poll> findByIdxModerator(String idxModerator);
  //List<Poll> findByTitle(String pollTitle);
}
