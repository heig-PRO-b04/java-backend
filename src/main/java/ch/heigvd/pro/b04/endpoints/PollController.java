package ch.heigvd.pro.b04.endpoints;

import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.polls.Poll;
import ch.heigvd.pro.b04.polls.PollIdentifier;
import ch.heigvd.pro.b04.polls.PollRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PollController {

  @Autowired
  private final PollRepository repository;

  public PollController(PollRepository repository) {
    this.repository = repository;
  }

  @RequestMapping(value = "/poll", method = RequestMethod.GET)
  public List<Poll> all() {
    return repository.findAll();
  }

  /**
   * Returns a {@link Poll} for a certain identifier.
   *
   * @param moderator The moderator identifier of the poll.
   * @param poll      The poll identifier.
   * @return A {@link Poll} instance, if it exists.
   */
  @RequestMapping(value = "/poll/{idModerator}/{idPoll}", method = RequestMethod.GET)
  public Poll byId(@PathVariable("idModerator") Moderator moderator,
      @PathVariable("idPoll") Long poll) {
    PollIdentifier pollId = new PollIdentifier(poll);
    pollId.setIdxModerator(moderator);

    return repository
        .findById(pollId)
        .orElseThrow(IllegalArgumentException::new);
  }
}
