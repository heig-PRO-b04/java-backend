package ch.heigvd.pro.b04.polls;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.heigvd.pro.b04.auth.exceptions.WrongCredentialsException;
import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;
import ch.heigvd.pro.b04.messages.ServerMessage;
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
public class ServerPollControllerTest {

  @InjectMocks
  PollController controller;

  @Mock
  ModeratorRepository moderators;

  @Mock
  ServerPollRepository polls;

  @Test
  public void testAllPollsKnownModeratorWithCorrectCredentialsWorks() {

    Moderator moderator = Moderator.builder()
        .idModerator(1)
        .username("username")
        .token("secret")
        .build();

    List<ServerPoll> expected = List.of(new ServerPoll());

    when(moderators.findById(1)).thenReturn(Optional.of(moderator));
    when(moderators.findByToken("secret")).thenReturn(Optional.of(moderator));
    when(polls.findAllByModerator(moderator)).thenReturn(expected);

    assertDoesNotThrow(() -> {
      List<ServerPoll> response = controller.all("secret", 1);
      assertEquals(expected, response);
    });
  }

  @Test
  public void testAllPollsUnknownModeratorWithCorrectCredentialsDoesNotWork() {
    Moderator existingModerator = Moderator.builder()
        .idModerator(42)
        .username("username")
        .token("secret")
        .build();

    when(moderators.findById(1)).thenReturn(Optional.empty());
    when(moderators.findByToken("secret")).thenReturn(Optional.of(existingModerator));

    assertThrows(WrongCredentialsException.class, () -> controller.all("secret", 1));
  }

  @Test
  public void testKnownModeratorWithIncorrectCredentialsDoesNotWork() {
    Moderator existingModerator = Moderator.builder()
        .idModerator(42)
        .username("username")
        .token("secret")
        .build();

    when(moderators.findById(42)).thenReturn(Optional.of(existingModerator));
    when(moderators.findByToken("top secret")).thenReturn(Optional.empty());

    assertThrows(WrongCredentialsException.class, () -> controller.all("top secret", 42));
  }

  @Test
  public void testInsertNewPollWorksProperly() {

    Moderator moderator = Moderator.builder()
        .idModerator(1)
        .username("username")
        .token("token")
        .build();

    when(moderators.findByToken("token")).thenReturn(Optional.of(moderator));
    when(moderators.findById(1)).thenReturn(Optional.of(moderator));
    when(polls.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    ClientPoll request = ClientPoll.builder()
        .title("Sample title")
        .build();

    assertDoesNotThrow(() -> {
      ServerPoll response = controller.insert("token", 1, request);

      assertEquals(response.getTitle(), request.getTitle());
      assertEquals(response.getIdPoll().getIdxModerator().getIdModerator(), 1);
    });
  }

  @Test
  public void testInsertNewPollWithIncorrectTokenDoesNotWork() {

    Moderator moderator = Moderator.builder()
        .idModerator(1)
        .username("username")
        .token("token")
        .build();

    when(moderators.findById(1)).thenReturn(Optional.of(moderator));
    when(moderators.findByToken("top token")).thenReturn(Optional.empty());

    ClientPoll request = ClientPoll.builder()
        .title("Sample title")
        .build();

    assertThrows(WrongCredentialsException.class, () -> {
      controller.insert("top token", 1, request);
    });
  }

  @Test
  public void testDeleteExistingPollWithCorrectTokenDoesWork() {

    Moderator moderator = Moderator.builder()
        .idModerator(1)
        .username("username")
        .token("token")
        .build();

    ServerPollIdentifier identifier = ServerPollIdentifier.builder()
        .idxModerator(moderator)
        .idPoll(1)
        .build();

    ServerPoll deleted = ServerPoll.builder()
        .idPoll(identifier)
        .title("Hello there")
        .build();

    when(moderators.findByToken("token")).thenReturn(Optional.of(moderator));
    when(polls.findById(identifier)).thenReturn(Optional.of(deleted));

    assertDoesNotThrow(() -> {
      ServerMessage message = controller.delete("token", 1, 1);
      assertEquals("Poll deleted", message.getMessage());
      verify(polls, times(1)).delete(deleted);
    });
  }

  @Test
  public void testDeleteNonExistingPollWithCorrectTokenDoesNotWork() {

    Moderator moderator = Moderator.builder()
        .idModerator(1)
        .username("username")
        .token("token")
        .build();

    ServerPollIdentifier identifier = ServerPollIdentifier.builder()
        .idxModerator(moderator)
        .idPoll(1)
        .build();

    when(moderators.findByToken("token")).thenReturn(Optional.of(moderator));
    when(polls.findById(identifier)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> {
      controller.delete("token", 1, 1);
    });
  }

  @Test
  public void testDeleteExistingPollWithIncorrectTokenDoesNotWork() {

    when(moderators.findByToken("top secret")).thenReturn(Optional.empty());

    assertThrows(WrongCredentialsException.class, () -> {
      controller.delete("top secret", 1, 1);
    });
  }

  @Test
  public void testDeleteExistingPollFromOtherUserWithCorrectTokenDoesNotWork() {

    Moderator alice = Moderator.builder()
        .idModerator(1)
        .username("alice")
        .token("aliceSecret")
        .build();

    Moderator mallory = Moderator.builder()
        .idModerator(2)
        .username("mallory")
        .token("mallorySecret")
        .build();

    ServerPollIdentifier identifier = ServerPollIdentifier.builder()
        .idxModerator(alice)
        .idPoll(1)
        .build();

    ServerPoll alicePoll = ServerPoll.builder()
        .idPoll(identifier)
        .title("Hello there")
        .build();

    lenient().when(moderators.findByToken("aliceSecret"))
        .thenReturn(Optional.of(alice));
    lenient().when(moderators.findByToken("mallorySecret"))
        .thenReturn(Optional.of(mallory));
    lenient().when(polls.findById(identifier)).thenReturn(Optional.of(alicePoll));

    assertThrows(WrongCredentialsException.class, () -> {
      controller.delete("mallorySecret", 1, 1);
    });
  }

  @Test
  public void testUpdateExistingPollWithCorrectTokenWorks() {
    Moderator alice = Moderator.builder()
        .idModerator(1)
        .username("alice")
        .token("bunny")
        .build();
    ServerPollIdentifier pollIdentifier = ServerPollIdentifier.builder()
        .idxModerator(alice)
        .idPoll(1)
        .build();
    ServerPoll poll = ServerPoll.builder()
        .idPoll(pollIdentifier)
        .title("Super Poll")
        .build();

    when(moderators.findByToken("bunny")).thenReturn(Optional.of(alice));
    when(polls.findById(pollIdentifier)).thenReturn(Optional.of(poll));
    when(polls.update(any(), any())).thenReturn(1);

    assertDoesNotThrow(() -> {
      ClientPoll update = ClientPoll.builder().title("Hyper Poll").build();
      ServerPoll response = controller.update("bunny", 1, 1, update);
      assertEquals(pollIdentifier, response.getIdPoll());
      verify(polls, times(1)).update(pollIdentifier, "Hyper Poll");
    });
  }

  @Test
  public void testUpdateExistingPollWithIncorrectTokenDoesNotWork() {
    when(moderators.findByToken("bunny")).thenReturn(Optional.empty());

    assertThrows(WrongCredentialsException.class, () -> {
      ClientPoll update = ClientPoll.builder().title("Hyper Poll").build();
      controller.update("bunny", 1, 1, update);
    });
  }

  @Test
  public void testUpdateMissingPollWithKnownTokenDoesNotWork() {
    Moderator alice = Moderator.builder()
        .idModerator(1)
        .username("alice")
        .secret("bunny")
        .build();
    ServerPollIdentifier pollIdentifier = ServerPollIdentifier.builder()
        .idxModerator(alice)
        .idPoll(1)
        .build();

    when(moderators.findByToken("bunny")).thenReturn(Optional.of(alice));
    lenient().when(polls.update(any(), any())).thenReturn(0);
    lenient().when(polls.findById(pollIdentifier)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> {
      ClientPoll update = ClientPoll.builder().title("Hyper Poll").build();
      controller.update("bunny", 1, 1, update);
    });
  }
}
