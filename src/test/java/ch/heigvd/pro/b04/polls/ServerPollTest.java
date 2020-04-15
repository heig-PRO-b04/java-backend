package ch.heigvd.pro.b04.polls;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import ch.heigvd.pro.b04.auth.Utils;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServerPollTest {

  @Mock
  ModeratorRepository repository;

  @Test
  public void testPollWithSameModeratorIdHasAccess() {

    String secret = "password";
    String token = Utils.hash(secret);

    Moderator moderator = Moderator.builder()
        .idModerator(123)
        .secret(secret)
        .build();

    when(repository.findBySecret(token)).thenReturn(Optional.of(moderator));

    ServerPollIdentifier identifier = ServerPollIdentifier.builder()
        .idPoll(42)
        .idxModerator(moderator)
        .build();

    ServerPoll poll = ServerPoll.builder().idPoll(identifier).build();

    assertTrue(ServerPoll.isAvailableWithToken(poll, token, repository));
  }

  @Test
  public void testPollWithDifferentModeratorIdHasNoAccess() {

    String secret = "password";
    String token = Utils.hash(secret);
    String wrongToken = "1234";

    Moderator moderator = Moderator.builder()
        .idModerator(123)
        .secret(secret)
        .build();

    when(repository.findBySecret(wrongToken)).thenReturn(Optional.empty());

    ServerPollIdentifier identifier = ServerPollIdentifier.builder()
        .idPoll(42)
        .idxModerator(moderator)
        .build();

    ServerPoll poll = ServerPoll.builder().idPoll(identifier).build();

    assertFalse(ServerPoll.isAvailableWithToken(poll, wrongToken, repository));
  }
}
