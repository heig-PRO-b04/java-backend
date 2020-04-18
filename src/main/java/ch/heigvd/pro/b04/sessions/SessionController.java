package ch.heigvd.pro.b04.sessions;

import static ch.heigvd.pro.b04.auth.TokenUtils.base64Encode;
import static ch.heigvd.pro.b04.auth.TokenUtils.generateRandomToken;

import ch.heigvd.pro.b04.auth.exceptions.WrongCredentialsException;
import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.participants.Participant;
import ch.heigvd.pro.b04.participants.ParticipantIdentifier;
import ch.heigvd.pro.b04.participants.ParticipantRepository;
import ch.heigvd.pro.b04.polls.ServerPoll;
import ch.heigvd.pro.b04.polls.ServerPollRepository;
import ch.heigvd.pro.b04.polls.exceptions.PollNotExistingException;
import ch.heigvd.pro.b04.sessions.exceptions.IllegalSessionStateException;
import ch.heigvd.pro.b04.sessions.exceptions.SessionCodeNotHexadecimalException;
import ch.heigvd.pro.b04.sessions.exceptions.SessionNotAvailableException;
import ch.heigvd.pro.b04.sessions.exceptions.SessionStateMustBeClosedFirstException;
import ch.heigvd.pro.b04.sessions.exceptions.SessionStateMustBeOpenedFirstException;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

  private final SessionRepository sessionRepository;
  private final ModeratorRepository moderatorRepository;
  private final ServerPollRepository serverPollRepository;
  private final ParticipantRepository participantRepository;

  /** SessionController constructor.
   *
   * @param sessionRepository The session repository
   * @param moderatorRepository The moderator repository
   * @param serverPollRepository The serverPoll repository
   * @param participantRepository The participant repository
   */
  public SessionController(
      SessionRepository sessionRepository,
      ModeratorRepository moderatorRepository,
      ServerPollRepository serverPollRepository,
      ParticipantRepository participantRepository
  ) {
    this.sessionRepository = sessionRepository;
    this.moderatorRepository = moderatorRepository;
    this.serverPollRepository = serverPollRepository;
    this.participantRepository = participantRepository;
  }

  /**
   * Connects a new User to a Session.
   *
   * @param codeReceived The code of the session
   * @return The token associated with the newly connected user
   * @throws SessionNotAvailableException If the session is closed or closed to newcomers
   * @throws ResourceNotFoundException    If the session does not exist
   */
  @Transactional
  @RequestMapping(value = "/connect", method = RequestMethod.POST)
  public UserToken byCode(@RequestBody SessionCode codeReceived)
      throws SessionNotAvailableException,
      ResourceNotFoundException,
      SessionCodeNotHexadecimalException {

    if (!SessionCode.conformsToFormat(codeReceived)) {
      throw new SessionCodeNotHexadecimalException();
    }

    Optional<ServerSession> resp = sessionRepository.findByCode(codeReceived.getHexadecimal());

    if (resp.orElseThrow(ResourceNotFoundException::new).getState() != SessionState.OPEN) {
      throw new SessionNotAvailableException();
    }

    String token;
    do {
      token = base64Encode(generateRandomToken());
    } while (participantRepository.findByToken(token).isPresent());

    Participant participant = new Participant().builder()
        .idParticipant(ParticipantIdentifier.builder()
            .idParticipant(Participant.getNewIdentifier(participantRepository))
            .idxServerSession(resp.get())
            .build())
        .token(token)
        .build();

    participantRepository.saveAndFlush(participant);

    return new UserToken(token);
  }

  /** This method/endpoint is used to update/create a session, particularly it's state.
   *
   * @param idModerator The moderator's id
   * @param idPoll The poll's id
   * @param token The moderators'token
   * @param clientSession The state to update to
   * @return Returns a serverSession, updated as demanded.
   * @throws WrongCredentialsException will be thrown if the credentials are wrong
   * @throws PollNotExistingException will be thrown if the poll given in arugment doesn't exist
   */
  @PutMapping(value = "/mod/{idModerator}/poll/{idPoll}/session")
  @Transactional
  public ServerSession putSession(
      @PathVariable(name = "idModerator") Integer idModerator,
      @PathVariable(name = "idPoll") Integer idPoll,
      @RequestParam(name = "token") String token,
      @RequestBody ClientSession clientSession)
      throws WrongCredentialsException,
      PollNotExistingException,
      SessionStateMustBeOpenedFirstException,
      SessionStateMustBeClosedFirstException {

    // 1. Authenticate moderator
    Optional<Moderator> modFromId = moderatorRepository.findById(idModerator);
    if (! modFromId.equals(moderatorRepository.findByToken(token))) {
      throw new WrongCredentialsException();
    }

    // 2. Check that the poll exists and that we have access
    List<ServerPoll> pollList = serverPollRepository.findByModeratorAndId(modFromId.get(), idPoll);
    if (pollList.isEmpty()) {
      throw new PollNotExistingException();
    }

    ServerPoll poll = pollList.get(0);

    Optional<ServerSession> last = poll.getLatestSession(sessionRepository);
    ServerSession session;

    SessionState askedState = clientSession.getState();

    if (last.isEmpty()) {
      if (! askedState.equals(SessionState.OPEN)) {
        throw new SessionStateMustBeOpenedFirstException();
      } else {
        session = poll.newSession(sessionRepository);
      }
    } else {
      session = last.get();
      if (session.getState().equals(SessionState.OPEN)) {
        switch (askedState) {
          case OPEN:
            throw new SessionStateMustBeClosedFirstException();
          case QUARANTINED:
            session.setState(askedState);
            break;
          case CLOSED:
            session.close();
            break;
          default:
            throw new IllegalSessionStateException();
        }
      } else if (session.getState().equals(SessionState.QUARANTINED)) {
        switch (askedState) {
          case OPEN:
          case QUARANTINED:
            throw new SessionStateMustBeClosedFirstException();
          case CLOSED:
            session.close();
            break;
          default:
            throw new IllegalSessionStateException();
        }
      } else if (session.getState().equals(SessionState.CLOSED)) {
        switch (askedState) {
          case OPEN:
            session = poll.newSession(sessionRepository);
            break;
          case QUARANTINED:
          case CLOSED:
            throw new SessionStateMustBeOpenedFirstException();
          default:
            throw new IllegalSessionStateException();
        }
      }
    }

    return sessionRepository.saveAndFlush(session);
  }
}