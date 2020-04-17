package ch.heigvd.pro.b04.auth;

import static ch.heigvd.pro.b04.auth.TokenUtils.base64Encode;
import static ch.heigvd.pro.b04.auth.TokenUtils.generateRandomSalt;
import static ch.heigvd.pro.b04.auth.TokenUtils.generateRandomToken;
import static ch.heigvd.pro.b04.auth.TokenUtils.getSecret;

import ch.heigvd.pro.b04.auth.exceptions.DuplicateUsernameException;
import ch.heigvd.pro.b04.auth.exceptions.InvalidCredentialsException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import javax.transaction.Transactional;
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
  @Transactional
  public TokenCredentials register(@RequestBody UserCredentials credentials)
      throws DuplicateUsernameException, InvalidCredentialsException {

    // Ensure that the username has a proper length.
    if (credentials.getUsername().length() < 4 || credentials.getPassword().length() < 4) {
      throw new InvalidCredentialsException();
    }

    // Does this username already exist ?
    if (moderators.findByUsername(credentials.getUsername()).isPresent()) {
      throw new DuplicateUsernameException();
    }

    byte[] salt = generateRandomSalt();
    String base64Salt = base64Encode(salt);
    String hashedSecret = getSecret(credentials.getPassword(), salt);
    String token = base64Encode(generateRandomToken());

    Moderator moderator = Moderator.builder()
        .username(credentials.getUsername())
        .secret(hashedSecret)
        .salt(base64Salt)
        .token(token)
        .build();

    try {
      Moderator inserted = moderators.saveAndFlush(moderator);
      return TokenCredentials.builder()
          .token(inserted.getToken())
          .idModerator(inserted.getIdModerator())
          .build();
    } catch (Throwable t) {
      throw new DuplicateUsernameException();
    }
  }
}
