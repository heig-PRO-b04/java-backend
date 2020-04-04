package ch.heigvd.pro.b04.auth;

import ch.heigvd.pro.b04.auth.exceptions.UnknownUserCredentialsException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import java.util.Optional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

  private ModeratorRepository moderators;

  public LoginController(ModeratorRepository moderators) {
    this.moderators = moderators;
  }

  /**
   * Logs a user in, provided that they give their username and their password.
   *
   * @param credentials The credentials object that is received.
   * @return An authentication token for the provided account.
   * @throws UnknownUserCredentialsException If the provided credentials are unknown to the app.
   */
  @RequestMapping(value = "login", method = RequestMethod.POST)
  @ResponseBody
  public TokenCredentials login(@RequestBody UserCredentials credentials)
      throws UnknownUserCredentialsException {
    // FIXME : Use proper token-based authentication, rather than returning the password of the
    //         user.
    Optional<Moderator> moderator = moderators.findByUsername(credentials.getUsername());
    Optional<TokenCredentials> response = moderator.flatMap(m -> {
      if (m.getSecret().equals(credentials.getPassword())) {
        return Optional.of(TokenCredentials.builder().token(m.getSecret()).build());
      } else {
        return Optional.empty();
      }
    });
    return response.orElseThrow(UnknownUserCredentialsException::new);
  }
}
