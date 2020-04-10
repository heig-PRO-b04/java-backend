package ch.heigvd.pro.b04.auth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RegistrationControllerTest {

  @InjectMocks
  RegistrationController registrationController;

  @Mock
  ModeratorRepository moderatorRepository;

  @Test
  public void testRegistrationWorks() {

    UserCredentials credentials = UserCredentials.builder()
        .username("sample")
        .password("password")
        .build();

    Moderator inserted = Moderator.builder()
        .username("sample")
        .secret(Utils.hash("password"))
        .build();

    Moderator expectedReturn = Moderator.builder()
        .username("sample")
        .secret(Utils.hash("password"))
        .idModerator(42)
        .build();

    when(moderatorRepository.saveAndFlush(inserted)).thenReturn(expectedReturn);

    assertDoesNotThrow(() -> {
      TokenCredentials token = registrationController.register(credentials);
      assertEquals(42, token.getIdModerator());
    });
  }

  @Test
  public void testRegistrationDoesNotWorkWithEmptyPassword() {

    UserCredentials credentials = UserCredentials.builder()
        .username("")
        .password("password")
        .build();

    assertThrows(Throwable.class, () -> registrationController.register(credentials));
  }

}
