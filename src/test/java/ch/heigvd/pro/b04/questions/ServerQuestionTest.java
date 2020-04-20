package ch.heigvd.pro.b04.questions;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import ch.heigvd.pro.b04.auth.exceptions.WrongCredentialsException;
import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.participants.Participant;
import ch.heigvd.pro.b04.participants.ParticipantIdentifier;
import ch.heigvd.pro.b04.participants.ParticipantRepository;
import ch.heigvd.pro.b04.polls.ServerPoll;
import ch.heigvd.pro.b04.polls.ServerPollIdentifier;
import ch.heigvd.pro.b04.polls.ServerPollRepository;
import ch.heigvd.pro.b04.polls.exceptions.PollNotExistingException;
import ch.heigvd.pro.b04.sessions.ServerSession;
import ch.heigvd.pro.b04.sessions.SessionIdentifier;
import ch.heigvd.pro.b04.sessions.SessionState;
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
  QuestionRepository questionRepository;

  @Mock
  ModeratorRepository modoRepo;

  @Mock
  ParticipantRepository participantRepository;

  @Test
  public void testModeratorCannotAccessQuestionsOfOtherModerators() {

    ServerPoll pollTemp = ServerPoll.builder()
        .idPoll(ServerPollIdentifier.builder().idPoll(123).build())
        .pollServerQuestions(Set.of()).build();
    ServerPoll pollTemp2 = ServerPoll.builder()
        .idPoll(ServerPollIdentifier.builder().idPoll(123).build())
        .pollServerQuestions(Set.of()).build();

    Moderator aloy = Moderator.builder()
        .idModerator(1)
        .username("aloy")
        .secret("chieftain")
        .pollSet(Set.of(pollTemp))
        .build();

    Moderator ikrie = Moderator.builder()
        .idModerator(2)
        .username("ikrie")
        .pollSet(Set.of(pollTemp2))
        .secret("banuk").build();

    when(pollRepo.findById(pollTemp.getIdPoll())).thenReturn(Optional.of(pollTemp));
    when(modoRepo.findByToken("t1")).thenReturn(Optional.of(aloy));
    when(modoRepo.findByToken("t2")).thenReturn(Optional.of(ikrie));
    when(participantRepository.findByToken("t1")).thenReturn(Optional.empty());
    when(participantRepository.findByToken("t2")).thenReturn(Optional.empty());

    assertDoesNotThrow(() -> cc.all(pollTemp.getIdPoll(), "t1", 1));
    assertThrows(WrongCredentialsException.class, () -> cc.all(pollTemp.getIdPoll(), "t2", 2));
  }

  @Test
  public void testAllEndpointsReturnAllQuestions()
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

    assertEquals(2, cc.all(pollTemp.getIdPoll(), "t1", 1).size());
    //with wrong token
    assertThrows(ResourceNotFoundException.class, () -> cc.all(pollTemp.getIdPoll(), "t2", 1));
  }

  @Test
  public void testByIdEndpointsReturnTheRightQuestions()
      throws WrongCredentialsException, ResourceNotFoundException, PollNotExistingException {
    ServerQuestion c1 = ServerQuestion.builder()
        .id(1)
        .title("Do you dream of Scorchers ?").build();
    ServerQuestion c2 = ServerQuestion.builder()
        .id(2)
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
    when(questionRepository.findById(new ServerQuestionIdentifier(1))).thenReturn(Optional.of(c1));

    assertEquals(c1, cc.byId("t1", pollTemp.getIdPoll(), new ServerQuestionIdentifier(1)));
    //with wrong idQuestion
    assertThrows(ResourceNotFoundException.class,
        () -> cc.byId("t1", pollTemp.getIdPoll(), new ServerQuestionIdentifier(3)));
  }

  @Test
  public void testParticipantCanAlsoAccessQuestions()
      throws WrongCredentialsException, ResourceNotFoundException, PollNotExistingException {
    ServerQuestion c1 = ServerQuestion.builder()
        .id(1).title("Do you dream of Scorchers ?").build();
    ServerQuestion c2 = ServerQuestion.builder()
        .id(2).title("Do you love the Frostclaws ?").build();

    ServerPoll pollTemp = ServerPoll.builder()
        .idPoll(ServerPollIdentifier.builder().idPoll(123).build())
        .pollServerQuestions(Set.of(c1, c2)).build();

    Moderator ikrie = Moderator.builder()
        .idModerator(2)
        .username("ikrie")
        .pollSet(Set.of(pollTemp))
        .secret("banuk").build();

    ServerSession s2 = ServerSession.builder()
        .idSession(SessionIdentifier.builder()
            .idSession(1).idxPoll(pollTemp).idxModerator(ikrie).build())
        .state(SessionState.OPEN).build();

    Participant aloy = Participant.builder()
        .idParticipant(ParticipantIdentifier.builder()
            .idParticipant(1).idxServerSession(s2).build())
        .token("t1").build();

    when(pollRepo.findById(pollTemp.getIdPoll())).thenReturn(Optional.of(pollTemp));
    lenient().when(modoRepo.findByToken("t2")).thenReturn(Optional.of(ikrie));
    when(participantRepository.findByToken("t1")).thenReturn(Optional.of(aloy));
    when(questionRepository.findById(new ServerQuestionIdentifier(1))).thenReturn(Optional.of(c1));

    assertEquals(2, cc.all(pollTemp.getIdPoll(), aloy.getToken(), 1).size());
    assertEquals(c1,
        cc.byId(aloy.getToken(), pollTemp.getIdPoll(), new ServerQuestionIdentifier(1)));
  }

  @Test
  public void testParticipantCannotInsertQuestion() {
    ServerQuestion c1 = ServerQuestion.builder()
        .id(1)
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
