package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.auth.exceptions.WrongCredentialsException;
import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;
import ch.heigvd.pro.b04.messages.ServerMessage;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
   * Returns a {@link List} of all the {@link ServerPoll} instances that are associated with a
   * certain moderator, with a certain token.
   *
   * @param token       The authentication token to use for the moderator.
   * @param idModerator The identifier of the moderator for which we query the polls.
   * @return The {@link List} of all the {@link ServerPoll}s of this moderator.
   * @throws WrongCredentialsException If the moderator is not known, or the credentials are not
   *                                   valid.
   */
  @RequestMapping(value = "/mod/{idModerator}/poll", method = RequestMethod.GET)
  public List<ServerPoll> all(
      @RequestParam(name = "token") String token,
      @PathVariable(name = "idModerator") Integer idModerator
  ) throws WrongCredentialsException {
    Optional<Moderator> moderatorForId = moderators.findById(idModerator);
    Optional<Moderator> moderatorForSecret = moderators.findByToken(token);

    if (!moderatorForId.equals(moderatorForSecret)) {
      throw new WrongCredentialsException();
    }

    Optional<List<ServerPoll>> pollsForModerator = moderatorForId.map(polls::findAllByModerator);
    return pollsForModerator.orElseThrow(WrongCredentialsException::new);
  }

  /**
   * Inserts a new poll for a given user.
   *
   * @param token       The authentication token for the user.
   * @param idModerator The identifier of the moderator who is adding the poll.
   * @param clientPoll  The data that should be written in the added poll-
   * @return The newly added poll, alongside with its identifier.
   * @throws WrongCredentialsException If the moderator is not known, or the credentials are not
   *                                   valid.
   */
  @PostMapping("/mod/{idModerator}/poll")
  public ServerPoll insert(
      @RequestParam(name = "token") String token,
      @PathVariable(name = "idModerator") Integer idModerator,
      @RequestBody ClientPoll clientPoll)
      throws WrongCredentialsException {
    Optional<Moderator> moderatorForId = moderators.findById(idModerator);
    Optional<Moderator> moderatorForSecret = moderators.findByToken(token);

    if (!moderatorForId.equals(moderatorForSecret)) {
      throw new WrongCredentialsException();
    }

    return moderatorForId
        .map(moderator -> moderator.newPoll(polls, clientPoll))
        .orElseThrow(WrongCredentialsException::new);
  }

  /**
   * Deletes a new poll for a given user.
   *
   * @param token       The authentication token for the user.
   * @param idModerator The identifier of the moderator who is deleting the poll.
   * @param idPoll      The identifier of the poll to delete.
   * @return A message indicating whether the deletion was successful or not.
   * @throws WrongCredentialsException If the moderator is invalid, or the credentials incorrect.
   * @throws ResourceNotFoundException If the poll that we're trying to delete does not exist.
   */
  @DeleteMapping("/mod/{idModerator}/poll/{idPoll}")
  public ServerMessage delete(
      @RequestParam(name = "token") String token,
      @PathVariable(name = "idModerator") Integer idModerator,
      @PathVariable(name = "idPoll") Integer idPoll
  ) throws WrongCredentialsException, ResourceNotFoundException {

    ServerPollIdentifier identifier = moderators.findByToken(token)
        .filter(moderator -> moderator.getIdModerator() == idModerator)
        .map(moderator -> ServerPollIdentifier.builder()
            .idxModerator(moderator)
            .idPoll(idPoll)
            .build()
        )
        .orElseThrow(WrongCredentialsException::new);

    ServerPoll poll = polls.findById(identifier)
        .orElseThrow(ResourceNotFoundException::new);

    polls.delete(poll);

    return ServerMessage.builder().message("Poll deleted").build();
  }
}
