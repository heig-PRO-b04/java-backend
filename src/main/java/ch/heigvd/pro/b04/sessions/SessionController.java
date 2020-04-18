package ch.heigvd.pro.b04.sessions;

import static ch.heigvd.pro.b04.auth.TokenUtils.base64Encode;
import static ch.heigvd.pro.b04.auth.TokenUtils.generateRandomToken;

import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.participants.Participant;
import ch.heigvd.pro.b04.participants.ParticipantIdentifier;
import ch.heigvd.pro.b04.participants.ParticipantRepository;
import ch.heigvd.pro.b04.sessions.Session.State;
import ch.heigvd.pro.b04.sessions.exceptions.SessionCodeNotHexadecimalException;
import ch.heigvd.pro.b04.sessions.exceptions.SessionNotAvailableException;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

  private SessionRepository sessionRepository;
  private ParticipantRepository participantRepository;

  public SessionController(SessionRepository repo, ParticipantRepository participantRepository) {
    this.sessionRepository = repo;
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
}