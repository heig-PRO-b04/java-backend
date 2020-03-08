package ch.heigvd.pro.b04.polls;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

  @RequestMapping(value = "/poll/{idModerator}/{idPoll}", method = RequestMethod.GET)
  public Poll byId(@PathVariable("idModerator") Long moderator, @PathVariable("idPoll") Long poll) {
    return repository
        .findById(new PollIdentifier(moderator, poll))
        .orElseThrow(IllegalArgumentException::new);
  }
}
