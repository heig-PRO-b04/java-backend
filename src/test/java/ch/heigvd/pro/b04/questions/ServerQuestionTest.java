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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    assertThrows(WrongCredentialsException.class, () -> cc.all(1, 123, "malloryToken"));
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

    assertDoesNotThrow(() -> assertEquals(List.of(q1, q2), cc.all(1, 123, "aliceToken")));
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

    assertThrows(WrongCredentialsException.class, () -> cc.insert(1, 123, "t1", c2));
    assertThrows(WrongCredentialsException.class, () -> cc.updateQuestion(1, 123, 1, "t1", c2));
    assertThrows(WrongCredentialsException.class, () -> cc.deleteQuestion(1, 123, 1, "t1"));
  }

  @Test
  public void testModeratorCanInsertOrUpdateQuestionOnlyWithRightAccess()
      throws ResourceNotFoundException, WrongCredentialsException {
    ClientQuestion c1 = ClientQuestion.builder()
        .title("Do you dream of Scorchers ?").build();

    ServerPoll pollTemp = ServerPoll.builder()
        .idPoll(ServerPollIdentifier.builder().idPoll(123).build())
        .build();

    Moderator aloy = Moderator.builder()
        .idModerator(1)
        .username("aloy")
        .secret("chieftain")
        .pollSet(Set.of(pollTemp))
        .build();
    pollTemp.getIdPoll().setIdxModerator(aloy);

    Moderator ikrie = Moderator.builder()
        .idModerator(2)
        .username("ikrie")
        .secret("banuk")
        .build();

    when(modoRepo.findByToken("t1")).thenReturn(Optional.of(aloy));
    when(modoRepo.findByToken("t2")).thenReturn(Optional.of(ikrie));
    when(pollRepo.findById(Mockito.any())).thenReturn(Optional.empty());
    when(pollRepo.findById(
        ServerPollIdentifier.builder()
        .idPoll(123).idxModerator(aloy).build()
    )).thenReturn(Optional.of(pollTemp));

    //Moderator who owns the poll
    assertDoesNotThrow(() -> cc.insert(1, 123, "t1", c1));
    //Moderator who does not own the poll
    assertThrows(ResourceNotFoundException.class, () ->cc.insert(2, 123, "t2", c1));

    ServerQuestion q2 = ServerQuestion.builder()
        .idServerQuestion(ServerQuestionIdentifier.builder()
            .idServerQuestion(2)
            .build())
        .title("Do you love the Frostclaws ?")
        .build();

    ServerPoll pollTemp2 = ServerPoll.builder()
        .idPoll(ServerPollIdentifier.builder().idPoll(223).build())
        .pollServerQuestions(Set.of(q2))
        .build();

    q2.getIdServerQuestion().setIdxPoll(pollTemp2);

    when(repo.findById(Mockito.any())).thenReturn(Optional.of(q2));
    when(pollRepo.findById(Mockito.any())).thenReturn(Optional.empty());
    when(pollRepo.findById(
        ServerPollIdentifier.builder()
            .idPoll(223).idxModerator(aloy).build()
    )).thenReturn(Optional.of(pollTemp));

    //Moderator who owns the poll
    assertEquals("Do you dream of Scorchers ?",
        cc.updateQuestion(1, 223, 2, "t1", c1).getTitle());
    //Moderator who does not own the poll
    assertThrows(ResourceNotFoundException.class,
        ()->cc.updateQuestion(2, 223, 2, "t2", c1));
  }
}