package ch.heigvd.pro.b04.account;

import static ch.heigvd.pro.b04.auth.TokenUtils.base64Decode;
import static ch.heigvd.pro.b04.auth.TokenUtils.base64Encode;
import static ch.heigvd.pro.b04.auth.TokenUtils.generateRandomSalt;
import static ch.heigvd.pro.b04.auth.TokenUtils.generateRandomToken;
import static ch.heigvd.pro.b04.auth.TokenUtils.getSecret;

import ch.heigvd.pro.b04.auth.exceptions.CredentialsTooShortException;
import ch.heigvd.pro.b04.auth.exceptions.UnknownUserCredentialsException;
import ch.heigvd.pro.b04.messages.ServerMessage;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import javax.transaction.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

  private final ModeratorRepository moderators;

  public AccountController(ModeratorRepository moderators) {
    this.moderators = moderators;
  }

  /**
   * Returns true if the provided password is the one set for the Moderator.
   *
   * @param moderator The moderator to check for.
   * @param candidate The candidate password.
   * @return True if the passwords match.
   */
  private boolean isCurrentPassword(Moderator moderator, String candidate) {
    return getSecret(candidate, base64Decode(moderator.getSalt()))
        .equals(moderator.getSecret());
  }

  /**
   * Changes the password of the moderator, and resets the authentication token.
   *
   * @param token       The authentication token of the user.
   * @param idModerator The moderator identifier.
   * @param password    The password information.
   * @return A success message.
   * @throws UnknownUserCredentialsException If authentication fails.
   * @throws CredentialsTooShortException    If the new password is too short.
   */
  @Transactional
  @PutMapping("/mod/{idModerator}/password")
  public ServerMessage changePassword(
      @RequestParam(name = "token") String token,
      @PathVariable(name = "idModerator") Integer idModerator,
      @RequestBody NewPassword password)
      throws UnknownUserCredentialsException, CredentialsTooShortException {

    Moderator moderator = moderators.findById(idModerator)
        .filter(found -> found.getToken().equals(token))
        .orElseThrow(UnknownUserCredentialsException::new);

    if (!isCurrentPassword(moderator, password.getCurrentPassword())) {
      throw new UnknownUserCredentialsException();
    }

    if (password.getNewPassword().length() < 4) {
      throw new CredentialsTooShortException();
    }

    String newSalt = base64Encode(generateRandomSalt());
    String newSecret = getSecret(password.getNewPassword(), base64Decode(newSalt));
    String newToken = base64Encode(generateRandomToken());

    Moderator updated = Moderator.builder()
        .idModerator(moderator.getIdModerator())
        .username(moderator.getUsername())
        .token(newToken)
        .salt(newSalt)
        .secret(newSecret)
        .build();

    moderators.saveAndFlush(updated);

    return ServerMessage.builder()
        .message("Password successfully updated.")
        .build();
  }
}
