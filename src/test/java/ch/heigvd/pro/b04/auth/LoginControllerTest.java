package ch.heigvd.pro.b04.auth;

import static ch.heigvd.pro.b04.auth.TokenUtils.base64Decode;
import static ch.heigvd.pro.b04.auth.TokenUtils.getSecret;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import ch.heigvd.pro.b04.auth.exceptions.UnknownUserCredentialsException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

  @InjectMocks
  LoginController loginController;

  @Mock
  ModeratorRepository moderatorRepository;

  @Test
  public void testLoginDoesNotWorkWithExistingAccount() {

    UserCredentials credentials = UserCredentials.builder()
        .username("unknown")
        .password("password")
        .build();

    assertThrows(UnknownUserCredentialsException.class, () -> loginController.login(credentials));
  }

  @Test
  public void testLoginWorksWithExistingAccountAndGoodPassword() {

    final int moderatorId = 123;

    UserCredentials credentials = UserCredentials.builder()
        .username("sample")
        .password("password")
        .build();

    when(moderatorRepository.findByUsername("sample"))
        .thenReturn(Optional.of(Moderator.builder()
            .username("sample")
            .secret(getSecret("password", base64Decode("salt")))
            .salt("salt")
            .token("token")
            .idModerator(moderatorId)
            .build())
        );

    assertDoesNotThrow(() -> {
      TokenCredentials response = loginController.login(credentials);
      assertEquals(moderatorId, response.getIdModerator());
      assertEquals("token", response.getToken());
    });
  }

  @Test
  public void testLoginDoesNotWorkWithExistingUsernameAndBadPassword() {

    UserCredentials credentials = UserCredentials.builder()
        .username("sample")
        .password("password")
        .build();

    when(moderatorRepository.findByUsername("sample"))
        .thenReturn(Optional.of(Moderator.builder()
            .username("sample")
            .secret(getSecret("another password", base64Decode("salt")))
            .salt("salt")
            .token("token")
            .build())
        );

    assertThrows(UnknownUserCredentialsException.class, () -> loginController.login(credentials));
  }
}
