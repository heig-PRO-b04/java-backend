package ch.heigvd.pro.b04.sessions;

import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;
import ch.heigvd.pro.b04.sessions.exceptions.SessionCodeNotHexadecimalException;
import ch.heigvd.pro.b04.sessions.exceptions.SessionNotAvailableException;
import java.util.Optional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

  private SessionRepository repository;

  public SessionController(SessionRepository repo) {
    repository = repo;
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

    Optional<Session> resp = repository.findByCode(codeReceived.getHexadecimal());

    if (resp.orElseThrow(ResourceNotFoundException::new).getState() != Session.SessionState.OPEN) {
      throw new SessionNotAvailableException();
    }

    return null;
  }
}