package ch.heigvd.pro.b04.accounts;

import static ch.heigvd.pro.b04.auth.TokenUtils.base64Decode;
import static ch.heigvd.pro.b04.auth.TokenUtils.base64Encode;
import static ch.heigvd.pro.b04.auth.TokenUtils.generateRandomSalt;
import static ch.heigvd.pro.b04.auth.TokenUtils.generateRandomToken;
import static ch.heigvd.pro.b04.auth.TokenUtils.getSecret;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import ch.heigvd.pro.b04.account.AccountController;
import ch.heigvd.pro.b04.account.CurrentPassword;
import ch.heigvd.pro.b04.account.NewPassword;
import ch.heigvd.pro.b04.account.NewUsername;
import ch.heigvd.pro.b04.auth.exceptions.CredentialsTooShortException;
import ch.heigvd.pro.b04.auth.exceptions.DuplicateUsernameException;
import ch.heigvd.pro.b04.auth.exceptions.UnknownUserCredentialsException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountTest {

  @InjectMocks
  AccountController ac;

  @Mock
  ModeratorRepository moderatorRepository;

  private final String saltT = new String("HZD2017");

  @Test
  public void testModeratorCanChangeFieldWithGoodParams() {
    Moderator aloy = Moderator.builder()
        .idModerator(1)
        .username("aloy")
        .salt(saltT)
        .secret(getSecret("chieftain", base64Decode(saltT)))
        .token("t1").build();

    when(moderatorRepository.findById(1)).thenReturn(Optional.of(aloy));
    lenient().when(moderatorRepository.findByUsername("aloy")).thenReturn(Optional.of(aloy));
    when(moderatorRepository.findByUsername("chieftain aloy")).thenReturn(Optional.empty());
    when(moderatorRepository.saveAndFlush(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

    assertDoesNotThrow(() -> ac.changeUsername("t1", aloy.getIdModerator(),
        NewUsername.builder()
            .currentPassword("chieftain")
            .username("chieftain aloy").build()));

    assertDoesNotThrow(() -> ac.changePassword("t1", aloy.getIdModerator(),
        NewPassword.builder()
            .currentPassword("chieftain")
            .newPassword("chieftain banuk").build()));
  }

  @Test
  public void testModeratorCannotChangeFieldWithWrongParams() {
    Moderator aloy = Moderator.builder()
        .idModerator(1)
        .username("aloy")
        .salt(saltT)
        .secret(getSecret("chieftain", base64Decode(saltT)))
        .token("t1").build();

    when(moderatorRepository.findById(1)).thenReturn(Optional.of(aloy));
    lenient().when(moderatorRepository.findByUsername("aloy")).thenReturn(Optional.of(aloy));
    lenient().when(moderatorRepository.findByUsername("chieftain aloy"))
        .thenReturn(Optional.empty());
    lenient().when(moderatorRepository.saveAndFlush(Mockito.any()))
        .then(AdditionalAnswers.returnsFirstArg());

    //wrong pwd
    assertThrows(UnknownUserCredentialsException.class,
        () -> ac.changeUsername("t1", aloy.getIdModerator(),
            NewUsername.builder()
                .currentPassword("banuk")
                .username("chieftain aloy").build()));

    //wrong token
    assertThrows(UnknownUserCredentialsException.class,
        () -> ac.changeUsername("t11", aloy.getIdModerator(),
            NewUsername.builder()
                .currentPassword("chieftain")
                .username("chieftain aloy").build()));

    //wrong pwd
    assertThrows(UnknownUserCredentialsException.class,
        () -> ac.changeUsername("t1", aloy.getIdModerator(),
            NewUsername.builder()
                .currentPassword("cheiftain")
                .username("chieftain aloy").build()));

    //duplicate username
    assertThrows(DuplicateUsernameException.class,
        () -> ac.changeUsername("t1", aloy.getIdModerator(),
            NewUsername.builder()
                .currentPassword("chieftain")
                .username("aloy").build()));

    //new username too short
    assertThrows(CredentialsTooShortException.class,
        () -> ac.changeUsername("t1", aloy.getIdModerator(),
            NewUsername.builder()
                .currentPassword("chieftain")
                .username("tfw").build()));

    //wrong token
    assertThrows(UnknownUserCredentialsException.class,
        () -> ac.changePassword("t11", aloy.getIdModerator(),
            NewPassword.builder()
                .currentPassword("chieftain")
                .newPassword("chieftain banuk").build()));

    //wrong pwd
    assertThrows(UnknownUserCredentialsException.class,
        () -> ac.changePassword("t1", aloy.getIdModerator(),
            NewPassword.builder()
                .currentPassword("cheiftain")
                .newPassword("chieftain banuk").build()));

    //new password too short
    assertThrows(CredentialsTooShortException.class,
        () -> ac.changePassword("t1", aloy.getIdModerator(),
            NewPassword.builder()
                .currentPassword("chieftain")
                .newPassword("tfw").build()));
  }

  @Test
  public void testModeratorCanDeleteHisAccountWithRightParamsOnly() {
    Moderator aloy = Moderator.builder()
        .idModerator(1)
        .username("aloy")
        .salt(saltT)
        .secret(getSecret("chieftain", base64Decode(saltT)))
        .token("t1").build();

    when(moderatorRepository.findById(1)).thenReturn(Optional.of(aloy));
    lenient().when(moderatorRepository.findByUsername("aloy")).thenReturn(Optional.of(aloy));
    lenient().when(moderatorRepository.findByUsername("chieftain aloy"))
        .thenReturn(Optional.empty());

    assertDoesNotThrow(() -> ac.delete("t1", aloy.getIdModerator(),
        CurrentPassword.builder()
            .currentPassword("chieftain").build()));

    //wrong token
    assertThrows(UnknownUserCredentialsException.class,
        () -> ac.delete("t11", aloy.getIdModerator(),
            CurrentPassword.builder()
                .currentPassword("chieftain").build()));

    //wrong pwd
    assertThrows(UnknownUserCredentialsException.class, () -> ac.delete("t1", aloy.getIdModerator(),
        CurrentPassword.builder()
            .currentPassword("cheiftain").build()));
  }
}
