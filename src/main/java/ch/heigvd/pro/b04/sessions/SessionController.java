package ch.heigvd.pro.b04.sessions;

import ch.heigvd.pro.b04.sessions.exceptions.SessionNotAvailableException;
import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;
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
  public Session byCode(@RequestBody SessionCode codeReceived)
      throws SessionNotAvailableException, ResourceNotFoundException {
    Optional<Session> resp = repository.findByCode(codeReceived.getHexadecimal());

    if (resp.orElseThrow(ResourceNotFoundException::new).getState() != SessionState.OPEN) {
      throw new SessionNotAvailableException();
    }

    return resp.get();
  }
}