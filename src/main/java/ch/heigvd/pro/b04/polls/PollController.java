package ch.heigvd.pro.b04.polls;

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PollController {

  private PollRepository repository;

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
  public Poll byId(@PathVariable("idModerator") Long moderator, @PathVariable("idPoll") Long poll) {
    return repository
        .findById(new PollIdentifier(moderator, poll))
        .orElseThrow(IllegalArgumentException::new);
  }
}
