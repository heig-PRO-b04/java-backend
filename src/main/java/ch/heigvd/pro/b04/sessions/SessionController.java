package ch.heigvd.pro.b04.sessions;

import ch.heigvd.pro.b04.Constants.SessionState;
import ch.heigvd.pro.b04.auth.exceptions.SessionNotAvailableException;
import ch.heigvd.pro.b04.auth.exceptions.SessionNotExistingException;
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

  @RequestMapping(value = "/connect", method = RequestMethod.POST)
  Session byCode(@RequestBody String codeReceived)
      throws SessionNotAvailableException, SessionNotExistingException {
    Optional<Session> resp = repository.findByCode(codeReceived);
    if (resp.get().getState() != SessionState.OPEN) {
      throw new SessionNotAvailableException();
    }

    return resp.orElseThrow(SessionNotExistingException::new);
  }
}