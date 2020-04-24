package ch.heigvd.pro.b04.questions;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import ch.heigvd.pro.b04.auth.exceptions.WrongCredentialsException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.participants.Participant;
import ch.heigvd.pro.b04.participants.ParticipantRepository;
import ch.heigvd.pro.b04.polls.ServerPoll;
import ch.heigvd.pro.b04.polls.ServerPollIdentifier;
import ch.heigvd.pro.b04.polls.ServerPollRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    Moderator alice = Moderator.builder()
        .idModerator(1)
        .username("alice")
        .token("aliceToken")
        .build();

    Moderator mallory = Moderator.builder()
        .idModerator(2)
        .username("bob")
        .token("malloryToken")
        .build();

    ServerPollIdentifier identifier = ServerPollIdentifier.builder()
        .idxModerator(alice)
        .idPoll(123)
        .build();

    ServerPoll pollTemp = ServerPoll.builder()
        .idPoll(identifier)
        .build();

    when(participantRepository.findByToken("malloryToken")).thenReturn(Optional.empty());
    when(modoRepo.findByToken("malloryToken")).thenReturn(Optional.of(mallory));
    lenient().when(pollRepo.findById(identifier)).thenReturn(Optional.of(pollTemp));

    assertThrows(WrongCredentialsException.class, () -> cc.all(1, "malloryToken", 123));
  }

  @Test
  public void testAllEndpointsReturnAllQuestions() {

    Moderator alice = Moderator.builder()
        .idModerator(1)
        .username("alice")
        .token("aliceToken")
        .build();

    ServerPollIdentifier identifier = ServerPollIdentifier.builder()
        .idxModerator(alice)
        .idPoll(123)
        .build();

    ServerPoll poll = ServerPoll.builder()
        .idPoll(identifier)
        .title("Hello world.")
        .build();

    ServerQuestionIdentifier qi1 = ServerQuestionIdentifier.builder()
        .idServerQuestion(1)
        .idxPoll(poll)
        .build();

    ServerQuestionIdentifier qi2 = ServerQuestionIdentifier.builder()
        .idServerQuestion(2)
        .idxPoll(poll)
        .build();

    ServerQuestion q1 = ServerQuestion.builder()
        .idServerQuestion(qi1)
        .title("Do you dream of Scorchers ?")
        .build();

    ServerQuestion q2 = ServerQuestion.builder()
        .idServerQuestion(qi2)
        .title("Do you love the Frostclaws ?")
        .build();

    // Data access.
    when(modoRepo.findById(1)).thenReturn(Optional.of(alice));
    when(pollRepo.findById(identifier)).thenReturn(Optional.of(poll));
    when(repo.findAll()).thenReturn(List.of(q1, q2));

    // Token management.
    lenient().when(modoRepo.findByToken("aliceToken")).thenReturn(Optional.of(alice));
    lenient().when(participantRepository.findByToken("aliceToken")).thenReturn(Optional.empty());

    assertDoesNotThrow(() -> assertEquals(List.of(q1, q2), cc.all(1, "aliceToken", 123)));
  }

  @Test
  public void testParticipantCannotInsertOrUpdateOrDeleteQuestion() {
    ServerQuestion c1 = ServerQuestion.builder()
        .idServerQuestion(ServerQuestionIdentifier.builder().idServerQuestion(1).build())
        .title("Do you dream of Scorchers ?").build();
    ClientQuestion c2 = ClientQuestion.builder()
        .title("Do you love the Frostclaws ?").build();

    ServerPoll pollTemp = ServerPoll.builder()
        .idPoll(ServerPollIdentifier.builder().idPoll(123).build())
        .pollServerQuestions(Set.of(c1)).build();

    Participant aloy = Participant.builder().build();

    lenient().when(modoRepo.findByToken("t1")).thenReturn(Optional.empty());
    lenient().when(participantRepository.findByToken("t1")).thenReturn(Optional.of(aloy));
    //lenient().when(repo.delete(Mockito.any())).thenReturn(new ServerMessage("success"));

    assertThrows(WrongCredentialsException.class, () -> cc.insert(1, "t1", 123, c2));
    assertThrows(WrongCredentialsException.class, ()->cc.updateQuestion(1, "t1", 123, 1,c2));
    assertThrows(WrongCredentialsException.class, ()->cc.deleteQuestion(1, "t1", 123, 1));
  }
}