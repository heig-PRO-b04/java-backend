package ch.heigvd.pro.b04.polls;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import ch.heigvd.pro.b04.auth.exceptions.WrongCredentialsException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PollControllerTest {

  @InjectMocks
  PollController controller;

  @Mock
  ModeratorRepository moderators;

  @Mock
  PollRepository polls;

  @Test
  public void testAllPollsKnownModeratorWithCorrectCredentialsWorks() {

    Moderator moderator = Moderator.builder()
        .idModerator(1)
        .username("username")
        .secret("secret")
        .build();

    List<Poll> expected = List.of(new Poll());

    when(moderators.findById(1)).thenReturn(Optional.of(moderator));
    when(moderators.findBySecret("secret")).thenReturn(Optional.of(moderator));
    when(polls.findAllByModerator(moderator)).thenReturn(expected);

    assertDoesNotThrow(() -> {
      List<Poll> response = controller.all("secret", 1);
      assertEquals(expected, response);
    });
  }

  @Test
  public void testAllPollsUnknownModeratorWithCorrectCredentialsDoesNotWork() {
    Moderator existingModerator = Moderator.builder()
        .idModerator(42)
        .username("username")
        .secret("secret")
        .build();

    when(moderators.findById(1)).thenReturn(Optional.empty());
    when(moderators.findBySecret("secret")).thenReturn(Optional.of(existingModerator));

    assertThrows(WrongCredentialsException.class, () -> controller.all("secret", 1));
  }

  @Test
  public void testKnownModeratorWithIncorrectCredentialsDoesNotWork() {
    Moderator existingModerator = Moderator.builder()
        .idModerator(42)
        .username("username")
        .secret("secret")
        .build();

    when(moderators.findById(42)).thenReturn(Optional.of(existingModerator));
    when(moderators.findBySecret("top secret")).thenReturn(Optional.empty());

    assertThrows(WrongCredentialsException.class, () -> controller.all("top secret", 42));
  }
}
