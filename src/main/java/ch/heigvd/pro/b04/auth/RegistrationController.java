package ch.heigvd.pro.b04.auth;

import ch.heigvd.pro.b04.auth.exceptions.DuplicateUsernameException;
import ch.heigvd.pro.b04.auth.exceptions.InvalidCredentialsException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

  private ModeratorRepository moderators;

  public RegistrationController(ModeratorRepository moderators) {
    this.moderators = moderators;
  }

  /**
   * Registers a new user, assuming we got some user credentials.
   *
   * @param credentials The credentials to use for registration.
   * @return An authentication token for the provided account.
   */
  @RequestMapping(value = "register", method = RequestMethod.POST)
  public TokenCredentials register(@RequestBody UserCredentials credentials)
      throws DuplicateUsernameException, InvalidCredentialsException {
    // Ensure that the username has a proper length.
    if (credentials.getUsername().length() < 4 || credentials.getPassword().length() < 4) {
      throw new InvalidCredentialsException();
    }
    String hashed = Utils.hash(credentials.getPassword());
    Moderator moderator = Moderator.builder()
        .username(credentials.getUsername())
        .secret(hashed)
        .build();
    try {
      moderators.saveAndFlush(moderator);
    } catch (Throwable t) {
      throw new DuplicateUsernameException();
    }
    return TokenCredentials.builder().token(credentials.getPassword()).build();
  }
}
