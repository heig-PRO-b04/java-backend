package ch.heigvd.pro.b04.auth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import ch.heigvd.pro.b04.auth.exceptions.UnknownUserCredentialsException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.utils.Utils;
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
            .secret(Utils.hash("password"))
            .idModerator(moderatorId)
            .build())
        );

    assertDoesNotThrow(() -> {
      TokenCredentials response = loginController.login(credentials);
      assertEquals(moderatorId, response.getIdModerator());
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
            .secret("another password")
            .build())
        );

    assertThrows(UnknownUserCredentialsException.class, () -> loginController.login(credentials));
  }

  @Test
  public void testLoginFindsTwoModeratorsWithSameTokenHash() {

    UserCredentials loggedIn = UserCredentials.builder()
        .username("u1")
        .password("password")
        .build();

    Moderator first = Moderator.builder()
        .idModerator(1)
        .username("u1")
        .secret(Utils.hash("password"))
        .build();

    Moderator second = Moderator.builder()
        .idModerator(2)
        .username("u2")
        .secret(Utils.hash("password"))
        .build();

    lenient().when(moderatorRepository.findByUsername("u1"))
        .thenReturn(Optional.of(first));

    lenient().when(moderatorRepository.findByUsername("u2"))
        .thenReturn(Optional.of(second));

    // Find the second user.
    lenient().when(moderatorRepository.findBySecret(Utils.hash("password")))
        .thenReturn(Optional.of(second));

    TokenCredentials credentials = loginController.login(loggedIn);
    assertEquals(1, credentials.getIdModerator());
  }
}
