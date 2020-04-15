package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.auth.exceptions.WrongCredentialsException;
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

  private final ServerPollRepository polls;
  private final ModeratorRepository moderators;

  public PollController(
      ModeratorRepository moderators,
      ServerPollRepository polls
  ) {
    this.moderators = moderators;
    this.polls = polls;
  }

  /**
   * Returns a {@link List} of all the {@link ServerPoll} instances that are associated with a certain
   * moderator, with a certain token.
   *
   * @param token       The authentication token to use for the moderator.
   * @param idModerator The identifier of the moderator for which we query the polls.
   * @return The {@link List} of all the {@link ServerPoll}s of this moderator.
   * @throws WrongCredentialsException If the moderator is not known, or the credentials are
   *                                         not valid.
   */
  @RequestMapping(value = "/mod/{idModerator}/poll", method = RequestMethod.GET)
  public List<ServerPoll> all(
      @RequestParam(name = "token") String token,
      @PathVariable(name = "idModerator") Integer idModerator
  ) throws WrongCredentialsException {
    Optional<Moderator> moderatorForId = moderators.findById(idModerator);
    Optional<Moderator> moderatorForSecret = moderators.findBySecret(token);

    if (!moderatorForId.equals(moderatorForSecret)) {
      throw new WrongCredentialsException();
    }

    Optional<List<ServerPoll>> pollsForModerator = moderatorForId.map(polls::findAllByModerator);
    return pollsForModerator.orElseThrow(WrongCredentialsException::new);
  }
}
