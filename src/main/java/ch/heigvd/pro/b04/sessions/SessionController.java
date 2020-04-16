package ch.heigvd.pro.b04.sessions;

import ch.heigvd.pro.b04.auth.TokenUtils;
import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;
import ch.heigvd.pro.b04.participants.Participant;
import ch.heigvd.pro.b04.participants.ParticipantIdentifier;
import ch.heigvd.pro.b04.participants.ParticipantRepository;
import ch.heigvd.pro.b04.sessions.exceptions.SessionCodeNotHexadecimalException;
import ch.heigvd.pro.b04.sessions.exceptions.SessionNotAvailableException;
import java.util.Optional;
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
   * @param codeReceived The code of the session
   * @return The token associated with the newly connected user
   * @throws SessionNotAvailableException If the session is closed or closed to newcomers
   * @throws ResourceNotFoundException If the session does not exist
   */
  @RequestMapping(value = "/connect", method = RequestMethod.POST)
  public UserToken byCode(@RequestBody SessionCode codeReceived)
      throws SessionNotAvailableException,
             ResourceNotFoundException,
             SessionCodeNotHexadecimalException {

    if (! SessionCode.conformsToFormat(codeReceived)) {
      throw new SessionCodeNotHexadecimalException();
    }

    Optional<Session> resp = sessionRepository.findByCode(codeReceived.getHexadecimal());

    if (resp.orElseThrow(ResourceNotFoundException::new).getState() != Session.SessionState.OPEN) {
      throw new SessionNotAvailableException();
    }

    String token;
    do {
      token = TokenUtils.generateRandomToken().toString();
    } while (participantRepository.findByToken(token).isPresent());

    Participant participant = new Participant().builder()
        .idParticipant(ParticipantIdentifier.builder()
            .idParticipant(Participant.getNewIdentifier(participantRepository))
            .idxSession(resp.get())
            .build())
        .token(token)
        .build();

    participantRepository.saveAndFlush(participant);

    return new UserToken(token);
  }
}