package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.auth.exceptions.WrongCredentialsException;
import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;
import ch.heigvd.pro.b04.messages.ServerMessage;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.participants.Participant;
import ch.heigvd.pro.b04.participants.ParticipantRepository;
import ch.heigvd.pro.b04.sessions.SessionState;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PollController {

  private final ServerPollRepository polls;
  private final ModeratorRepository moderators;
  private final ParticipantRepository participants;

  /**
   * Constructor for a new {@link PollController instance}.
   *
   * @param moderators   The moderators repository.
   * @param polls        The polls repository.
   * @param participants The participants repository.
   */
  public PollController(
      ModeratorRepository moderators,
      ServerPollRepository polls,
      ParticipantRepository participants
  ) {
    this.moderators = moderators;
    this.polls = polls;
    this.participants = participants;
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
   * Returns an existing poll and displays its information.
   *
   * @param token       The authentication token for the user.
   * @param idModerator The identifier of the moderator who is updating the poll.
   * @param idPoll      The identifier of the poll to update.
   * @return The retrieved poll.
   * @throws WrongCredentialsException If the moderator is invalid, or the credentials incorrect.
   * @throws ResourceNotFoundException If the poll that we're trying to update does not exist.
   */
  @GetMapping("/mod/{idModerator}/poll/{idPoll}")
  public ServerPoll get(
      @RequestParam(name = "token") String token,
      @PathVariable(name = "idModerator") Integer idModerator,
      @PathVariable(name = "idPoll") Integer idPoll
  ) throws WrongCredentialsException, ResourceNotFoundException {

    Optional<Moderator> authenticatedModerator = moderators.findByToken(token);
    Optional<Participant> authenticatedParticipant = participants.findByToken(token)
        .filter(p -> SessionState.OPEN == p.getIdParticipant()
            .getIdxServerSession()
            .getState());

    Moderator pollModerator = authenticatedModerator
        .orElse(authenticatedParticipant.map(
            participant -> participant
                .getIdParticipant()
                .getIdxServerSession()
                .getIdSession()
                .getIdxPoll()
                .getIdPoll()
                .getIdxModerator())
            .filter(moderator -> moderator.getIdModerator() == idModerator)
            .orElseThrow(WrongCredentialsException::new));

    ServerPollIdentifier identifier = ServerPollIdentifier.builder()
        .idxModerator(pollModerator)
        .idPoll(idPoll)
        .build();

    return polls.findById(identifier)
        .orElseThrow(ResourceNotFoundException::new);
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
   * Updates an existing poll with some new information.
   *
   * @param token       The authentication token for the user.
   * @param idModerator The identifier of the moderator who is updating the poll.
   * @param idPoll      The identifier of the poll to update.
   * @param clientPoll  The data with which the poll should be updated.
   * @return The updated poll.
   * @throws WrongCredentialsException If the moderator is invalid, or the credentials incorrect.
   * @throws ResourceNotFoundException If the poll that we're trying to update does not exist.
   */
  @PutMapping("/mod/{idModerator}/poll/{idPoll}")
  public ServerPoll update(
      @RequestParam(name = "token") String token,
      @PathVariable(name = "idModerator") Integer idModerator,
      @PathVariable(name = "idPoll") Integer idPoll,
      @RequestBody ClientPoll clientPoll
  ) throws WrongCredentialsException, ResourceNotFoundException {

    ServerPollIdentifier identifier = moderators.findByToken(token)
        .filter(moderator -> moderator.getIdModerator() == idModerator)
        .map(moderator -> moderator.getPollIdentifier(idPoll))
        .orElseThrow(WrongCredentialsException::new);

    int changed = polls.update(identifier, clientPoll.getTitle());

    if (changed != 1) {
      throw new ResourceNotFoundException();
    }

    return polls.findById(identifier).orElseThrow(ResourceNotFoundException::new);
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
        .map(moderator -> moderator.getPollIdentifier(idPoll))
        .orElseThrow(WrongCredentialsException::new);

    ServerPoll poll = polls.findById(identifier)
        .orElseThrow(ResourceNotFoundException::new);

    polls.delete(poll);

    return ServerMessage.builder().message("Poll deleted").build();
  }
}
