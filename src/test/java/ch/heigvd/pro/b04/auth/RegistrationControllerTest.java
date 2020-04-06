package ch.heigvd.pro.b04.auth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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

    assertDoesNotThrow(() -> registrationController.register(credentials));
  }

}
