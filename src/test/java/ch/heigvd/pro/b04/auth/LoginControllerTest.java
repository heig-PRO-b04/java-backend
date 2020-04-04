package ch.heigvd.pro.b04.auth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
  public void testRegistrationWorks() {

    UserCredentials credentials = UserCredentials.builder()
        .username("sample")
        .password("password")
        .build();

    assertDoesNotThrow(() -> loginController.register(credentials));
  }

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

    UserCredentials credentials = UserCredentials.builder()
        .username("sample")
        .password("password")
        .build();

    when(moderatorRepository.findByUsername("sample"))
        .thenReturn(Optional.of(Moderator.builder()
            .username("sample")
            .secret("password")
            .build())
        );

    assertDoesNotThrow(() -> loginController.login(credentials));
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
}
