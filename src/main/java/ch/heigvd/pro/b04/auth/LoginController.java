package ch.heigvd.pro.b04.auth;

import ch.heigvd.pro.b04.auth.exceptions.UnknownUserCredentialsException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import java.util.Optional;
import javax.transaction.Transactional;
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
  @RequestMapping(value = "auth", method = RequestMethod.POST)
  @ResponseBody
  @Transactional
  public TokenCredentials login(@RequestBody UserCredentials credentials)
      throws UnknownUserCredentialsException {

    Optional<Moderator> moderator = moderators.findByUsername(credentials.getUsername());
    Optional<String> actualSecret = moderator.map(Moderator::getSalt)
        .map(TokenUtils::base64Decode)
        .map(salt -> TokenUtils.getSecret(credentials.getPassword(), salt));

    if (moderator.isPresent() && moderator.map(Moderator::getSecret).equals(actualSecret)) {
      return TokenCredentials.builder()
          .token(moderator.get().getToken())
          .idModerator(moderator.get().getIdModerator())
          .build();
    } else {
      throw new UnknownUserCredentialsException();
    }
  }
}
