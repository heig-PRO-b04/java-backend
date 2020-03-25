package ch.heigvd.pro.b04.polls;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class PollController {

    @Autowired private final PollRepository repository;

    public PollController(PollRepository repository) { this.repository = repository; }

    @RequestMapping(value = "/poll", method = RequestMethod.GET)
    public List<Poll> all() {
      return repository.findAll();
    }

    @RequestMapping(value = "/poll/{idModerator}/{idPoll}", method = RequestMethod.GET)
    public Poll byId(@PathVariable("idModerator") String moderator, @PathVariable("idPoll") Long poll) {
        PollIdentifier pollId=new PollIdentifier();
        pollId.setIdxModerator(moderator);

        return repository
            .findById(pollId)
            .orElseThrow(IllegalArgumentException::new);
    }
}
