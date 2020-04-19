package ch.heigvd.pro.b04.questions;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import ch.heigvd.pro.b04.auth.exceptions.WrongCredentialsException;
import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.participants.ParticipantRepository;
import ch.heigvd.pro.b04.polls.ServerPoll;
import ch.heigvd.pro.b04.polls.ServerPollIdentifier;
import ch.heigvd.pro.b04.polls.ServerPollRepository;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServerQuestionTest {

  @InjectMocks
  QuestionController cc;

  @Mock
  ServerPollRepository pollRepo;

  @Mock
  QuestionRepository repo;

  @Mock
  ModeratorRepository modoRepo;

  @Mock
  ParticipantRepository participantRepository;

  @Test
  public void testModeratorCannotAccessQuestionsOfOtherModerators() {

    ServerPoll pollTemp = ServerPoll.builder()
        .idPoll(ServerPollIdentifier.builder().idPoll(123).build()).build();
    ServerPoll pollTemp2 = ServerPoll.builder()
        .idPoll(ServerPollIdentifier.builder().idPoll(123).build()).build();

    Moderator aayla = Moderator.builder()
        .idModerator(1)
        .username("aayla")
        .secret("ryloth")
        .pollSet(Set.of(pollTemp))
        .build();

    Moderator talon = Moderator.builder()
        .idModerator(2)
        .username("talon")
        .pollSet(Set.of(pollTemp2))
        .secret("sith").build();

    when(pollRepo.findById(pollTemp.getIdPoll())).thenReturn(Optional.of(pollTemp));
    when(modoRepo.findByToken("t1")).thenReturn(Optional.of(aayla));
    when(modoRepo.findByToken("t2")).thenReturn(Optional.of(talon));
    when(participantRepository.findByToken("t1")).thenReturn(Optional.empty());
    when(participantRepository.findByToken("t2")).thenReturn(Optional.empty());

    assertDoesNotThrow(() -> cc.all(pollTemp.getIdPoll(), "t1", 1));
    assertThrows(WrongCredentialsException.class, () -> cc.all(pollTemp.getIdPoll(), "t2", 2));
  }
}
