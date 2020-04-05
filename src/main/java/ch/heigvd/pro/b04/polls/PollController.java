package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.auth.exceptions.UnknownUserCredentialsException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PollController {

  private final PollRepository polls;
  private final ModeratorRepository moderators;

  public PollController(
      ModeratorRepository moderators,
      PollRepository polls
  ) {
    this.moderators = moderators;
    this.polls = polls;
  }

  /**
   * Returns a {@link List} of all the {@link Poll} instances that are associated with a certain
   * moderator, with a certain token.
   *
   * @param token       The authentication token to use for the moderator.
   * @param idModerator The identifier of the moderator for which we query the polls.
   * @return The {@link List} of all the {@link Poll}s of this moderator.
   * @throws UnknownUserCredentialsException If the moderator is not known, or the credentials are
   *                                         not valid.
   */
  @RequestMapping(value = "/mod/{idModerator}/poll", method = RequestMethod.GET)
  public List<Poll> all(
      @RequestParam(name = "token") String token,
      @PathVariable(name = "idModerator") Integer idModerator
  ) throws UnknownUserCredentialsException {
    Optional<Moderator> moderatorForId = moderators.findById(idModerator);
    Optional<Moderator> moderatorForSecret = moderators.findBySecret(token);

    if (!moderatorForId.equals(moderatorForSecret)) {
      throw new UnknownUserCredentialsException();
    }

    Optional<List<Poll>> pollsForModerator = moderatorForId.map(polls::findAllByModerator);
    return pollsForModerator.orElseThrow(UnknownUserCredentialsException::new);
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

    return polls
        .findById(pollId)
        .orElseThrow(IllegalArgumentException::new);
  }
}
