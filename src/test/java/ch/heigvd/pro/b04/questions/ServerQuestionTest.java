package ch.heigvd.pro.b04.questions;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import ch.heigvd.pro.b04.auth.exceptions.WrongCredentialsException;
import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.participants.Participant;
import ch.heigvd.pro.b04.participants.ParticipantRepository;
import ch.heigvd.pro.b04.polls.ServerPoll;
import ch.heigvd.pro.b04.polls.ServerPollIdentifier;
import ch.heigvd.pro.b04.polls.ServerPollRepository;
import ch.heigvd.pro.b04.polls.exceptions.PollNotExistingException;
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

    assertThrows(WrongCredentialsException.class, () -> cc.all("malloryToken", 1, 123));
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
        .identifier(qi1)
        .title("Do you dream of Scorchers ?")
        .build();

    ServerQuestion q2 = ServerQuestion.builder()
        .identifier(qi2)
        .title("Do you love the Frostclaws ?")
        .build();

    // Data access.
    when(modoRepo.findById(1)).thenReturn(Optional.of(alice));
    when(pollRepo.findById(identifier)).thenReturn(Optional.of(poll));
    when(repo.findAll()).thenReturn(List.of(q1, q2));

    // Token management.
    when(modoRepo.findByToken("aliceToken")).thenReturn(Optional.of(alice));
    when(participantRepository.findByToken("aliceToken")).thenReturn(Optional.empty());

    assertDoesNotThrow(() -> assertEquals(List.of(q1, q2), cc.all("aliceToken", 1, 123)));
  }

  @Test
  public void testByIdEndpointsReturnTheRightQuestions()
      throws WrongCredentialsException, ResourceNotFoundException, PollNotExistingException {
    ServerQuestion c1 = ServerQuestion.builder()
        .title("Do you dream of Scorchers ?").build();
    ServerQuestion c2 = ServerQuestion.builder()
        .title("Do you love the Frostclaws ?").build();

    ServerPoll pollTemp = ServerPoll.builder()
        .idPoll(ServerPollIdentifier.builder().idPoll(123).build())
        .pollServerQuestions(Set.of(c1, c2)).build();

    Moderator aloy = Moderator.builder()
        .idModerator(1)
        .username("aloy")
        .secret("chieftain")
        .pollSet(Set.of(pollTemp))
        .build();

    when(pollRepo.findById(pollTemp.getIdPoll())).thenReturn(Optional.of(pollTemp));
    when(modoRepo.findByToken("t1")).thenReturn(Optional.of(aloy));
    when(participantRepository.findByToken("t1")).thenReturn(Optional.empty());
    when(repo.findById(new ServerQuestionIdentifier(1))).thenReturn(Optional.of(c1));

    assertEquals(c1, cc.byId("t1", pollTemp.getIdPoll(), new ServerQuestionIdentifier(1)));
    //with wrong idQuestion
    assertThrows(ResourceNotFoundException.class,
        () -> cc.byId("t1", pollTemp.getIdPoll(), new ServerQuestionIdentifier(3)));
  }

  @Test
  public void testParticipantCannotInsertQuestion() {
    ServerQuestion c1 = ServerQuestion.builder()
        .title("Do you dream of Scorchers ?").build();
    ClientQuestion c2 = ClientQuestion.builder()
        .title("Do you love the Frostclaws ?").build();

    ServerPoll pollTemp = ServerPoll.builder()
        .idPoll(ServerPollIdentifier.builder().idPoll(123).build())
        .pollServerQuestions(Set.of(c1)).build();

    Participant aloy = Participant.builder().build();

    lenient().when(modoRepo.findByToken("t1")).thenReturn(Optional.empty());
    lenient().when(participantRepository.findByToken("t1")).thenReturn(Optional.of(aloy));

    assertThrows(ResourceNotFoundException.class,
        () -> cc.insertQuestion("t1", c2, 1, pollTemp.getIdPoll()));
  }
}
