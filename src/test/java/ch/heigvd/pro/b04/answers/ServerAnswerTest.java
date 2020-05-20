package ch.heigvd.pro.b04.answers;

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
import ch.heigvd.pro.b04.participants.ParticipantIdentifier;
import ch.heigvd.pro.b04.participants.ParticipantRepository;
import ch.heigvd.pro.b04.polls.ServerPoll;
import ch.heigvd.pro.b04.polls.ServerPollIdentifier;
import ch.heigvd.pro.b04.polls.ServerPollRepository;
import ch.heigvd.pro.b04.questions.QuestionRepository;
import ch.heigvd.pro.b04.questions.ServerQuestion;
import ch.heigvd.pro.b04.questions.ServerQuestionIdentifier;
import ch.heigvd.pro.b04.sessions.ServerSession;
import ch.heigvd.pro.b04.sessions.SessionIdentifier;
import ch.heigvd.pro.b04.sessions.SessionState;
import ch.heigvd.pro.b04.votes.ServerVoteRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * All those tests are very similar to Question tests because Answers behave the same
 */
@ExtendWith(MockitoExtension.class)
public class ServerAnswerTest {

  @InjectMocks
  AnswerController ac;
  @Mock
  ServerPollRepository pollRepo;
  @Mock
  ModeratorRepository modoRepo;
  @Mock
  ParticipantRepository participantRepository;
  @Mock
  QuestionRepository questionRepository;
  @Mock
  AnswerRepository answerRepository;
  @Mock
  ServerVoteRepository voteRepository;

  @Test
  public void testModeratorCannotAccessAnswersOfOtherModerators() {

    Moderator aloy = Moderator.builder()
        .idModerator(1)
        .username("aloy")
        .token("chieftain")
        .build();

    Moderator ikrie = Moderator.builder()
        .idModerator(2)
        .username("ikrie")
        .token("banuk")
        .build();

    ServerPollIdentifier identifier = ServerPollIdentifier.builder()
        .idxModerator(aloy)
        .idPoll(123)
        .build();

    ServerPoll pollTemp = ServerPoll.builder()
        .idPoll(identifier)
        .build();

    ServerQuestionIdentifier qi1 = ServerQuestionIdentifier.builder()
        .idServerQuestion(1)
        .idxPoll(pollTemp)
        .build();

    ServerQuestion q1 = ServerQuestion.builder()
        .idServerQuestion(qi1)
        .title("Do you dream of Scorchers ?")
        .build();

    when(participantRepository.findByToken("banuk")).thenReturn(Optional.empty());
    when(participantRepository.findByToken("chieftain")).thenReturn(Optional.empty());
    when(modoRepo.findByToken("banuk")).thenReturn(Optional.of(ikrie));
    when(modoRepo.findByToken("chieftain")).thenReturn(Optional.of(aloy));
    when(modoRepo.findById(aloy.getIdModerator())).thenReturn(Optional.of(aloy));
    lenient().when(pollRepo.findById(identifier)).thenReturn(Optional.of(pollTemp));
    lenient().when(questionRepository.findById(qi1)).thenReturn(Optional.of(q1));

    //right token
    assertDoesNotThrow(() -> ac.all(1, 123, 1, "chieftain"));
    //wrong token
    assertThrows(WrongCredentialsException.class, () -> ac.all(1, 123, 1, "banuk"));
    //wrong parameters
    assertThrows(ResourceNotFoundException.class, () -> ac.all(1, 123, 3, "chieftain"));
  }

  @Test
  public void testAllAndByIdEndpointsReturnRequiredAnswers()
      throws ResourceNotFoundException, WrongCredentialsException {

    Moderator aloy = Moderator.builder()
        .idModerator(1)
        .username("aloy")
        .token("chieftain")
        .build();

    Moderator ikrie = Moderator.builder()
        .idModerator(2)
        .username("ikrie")
        .token("banuk")
        .build();

    ServerPollIdentifier identifier = ServerPollIdentifier.builder()
        .idxModerator(aloy)
        .idPoll(123)
        .build();

    ServerSession session=ServerSession.builder().build();

    ServerPoll poll = ServerPoll.builder()
        .idPoll(identifier)
        .serverSessionSet(Set.of(session))
        .build();

    ServerQuestionIdentifier qi1 = ServerQuestionIdentifier.builder()
        .idServerQuestion(1)
        .idxPoll(poll)
        .build();

    ServerQuestion q1 = ServerQuestion.builder()
        .idServerQuestion(qi1)
        .title("Do you dream of Scorchers ?")
        .build();

    ServerAnswerIdentifier identifier1 = ServerAnswerIdentifier.builder()
        .idAnswer(1)
        .idxServerQuestion(q1)
        .build();

    ServerAnswerIdentifier identifier2 = ServerAnswerIdentifier.builder()
        .idAnswer(2)
        .idxServerQuestion(q1)
        .build();

    ServerAnswer a1 = ServerAnswer.builder()
        .idAnswer(identifier1)
        .title("Every night :)")
        .build();

    ServerAnswer a2 = ServerAnswer.builder()
        .idAnswer(identifier2)
        .title("Just when I destroyed one")
        .build();

    session= ServerSession.builder()
        .idSession(SessionIdentifier.builder()
        .idSession(1)
        .idxPoll(poll).build())
        .state(SessionState.OPEN)
        .build();

    Participant varga= Participant.builder()
        .idParticipant(ParticipantIdentifier.builder()
            .idxServerSession(session)
            .idParticipant(1).build())
        .token("icerail").build();

    // Data access.
    when(modoRepo.findById(1)).thenReturn(Optional.of(aloy));
    when(modoRepo.findById(2)).thenReturn(Optional.of(ikrie));
    when(pollRepo.findById(identifier)).thenReturn(Optional.of(poll));
    when(questionRepository.findById(qi1)).thenReturn(Optional.of(q1));
    lenient().when(answerRepository.findAll())
        .thenReturn(List.of(a1, a2));
    when(answerRepository.findById(identifier1)).thenReturn(Optional.of(a1));
    when(answerRepository.findById(identifier2)).thenReturn(Optional.of(a2));

    // Token management.
    lenient().when(modoRepo.findByToken("chieftain")).thenReturn(Optional.of(aloy));
    lenient().when(modoRepo.findByToken("banuk")).thenReturn(Optional.of(ikrie));
    lenient().when(participantRepository.findByToken("chieftain")).thenReturn(Optional.empty());
    when(participantRepository.findByToken(varga.getToken())).thenReturn(Optional.of(varga));
    lenient().when(participantRepository.findById(varga.getIdParticipant())).thenReturn(Optional.of(varga));
    when(voteRepository.findAll()).thenReturn(List.of());

    //moderator token
    assertDoesNotThrow(() -> assertEquals(List.of(a1, a2), ac.all(1, 123, 1, "chieftain")));
    assertEquals(a1.getTitle(), ac.byId(1,123,1,1,aloy.getToken()).getTitle());
    assertEquals(a2.getTitle(), ac.byId(1,123,1,2,aloy.getToken()).getTitle());

    //participant token
    assertDoesNotThrow(() -> assertEquals(List.of(a1, a2), ac.all(1, 123, 1, varga.getToken())));
    assertEquals(a1.getTitle(), ac.byId(1,123,1,1,varga.getToken()).getTitle());

    //wrong tokens and params
    assertThrows(WrongCredentialsException.class, () -> assertEquals(List.of(a1, a2),
        ac.all(1, 123, 1, ikrie.getToken())));
    assertThrows(ResourceNotFoundException.class, () -> assertEquals(List.of(a1, a2),
        ac.all(2, 123, 1, ikrie.getToken())));
  }

  @Test
  public void testParticipantCannotInsertOrUpdateOrDeleteAnswer() {
    ClientAnswer a1 = ClientAnswer.builder()
        .title("Every night :)")
        .build();

    ServerAnswer a2 = ServerAnswer.builder()
        .idAnswer(ServerAnswerIdentifier.builder()
            .idAnswer(11).build())
        .title("Just when I destroyed one")
        .build();

    ServerQuestion c1 = ServerQuestion.builder()
        .idServerQuestion(ServerQuestionIdentifier.builder().idServerQuestion(1).build())
        .title("Do you dream of Scorchers ?").build();

    ServerPoll pollTemp = ServerPoll.builder()
        .idPoll(ServerPollIdentifier.builder().idPoll(123).build())
        .pollServerQuestions(Set.of(c1)).build();

    Participant aloy = Participant.builder().build();

    lenient().when(modoRepo.findByToken("t1")).thenReturn(Optional.empty());
    lenient().when(participantRepository.findByToken("t1")).thenReturn(Optional.of(aloy));

    assertThrows(WrongCredentialsException.class, () -> ac.insertAnswer(1, 123, 1, "t1", a1));
    assertThrows(WrongCredentialsException.class, () -> ac.updateAnswer(1, 123, 1, 11, "t1", a1));
    assertThrows(WrongCredentialsException.class, () -> ac.deleteAnswer(1, 123, 1, 11, "t1"));
  }

  @Test
  public void testModeratorCanInsertAnswerOnlyWithRightAccess() {
    ServerPoll pollTemp = ServerPoll.builder()
        .idPoll(ServerPollIdentifier.builder().idPoll(123).build())
        .build();

    ClientAnswer a1 = ClientAnswer.builder()
        .title("Every night :)")
        .build();

    ServerQuestion c1 = ServerQuestion.builder()
        .idServerQuestion(ServerQuestionIdentifier.builder()
            .idServerQuestion(1)
            .idxPoll(pollTemp).build())
        .title("Do you dream of Scorchers ?").build();

    Moderator aloy = Moderator.builder()
        .idModerator(1)
        .username("aloy")
        .secret("chieftain")
        .pollSet(Set.of(pollTemp))
        .build();
    pollTemp = ServerPoll.builder()
        .idPoll(ServerPollIdentifier.builder().idPoll(123)
            .idxModerator(aloy).build())
        .build();

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
    when(questionRepository.findById(ServerQuestionIdentifier.builder()
        .idServerQuestion(1)
        .idxPoll(pollTemp).build())).thenReturn(Optional.of(c1));

    //Moderator who owns the poll
    assertDoesNotThrow(() -> ac.insertAnswer(1, 123, 1, "t1", a1));
    //wrong params
    assertThrows(ResourceNotFoundException.class, () -> ac.insertAnswer(1, 124, 1, "t1", a1));
    //Moderator who does not own the poll
    assertThrows(ResourceNotFoundException.class, () -> ac.insertAnswer(2, 123, 1, "t2", a1));
    //wrong token
    assertThrows(WrongCredentialsException.class, () -> ac.insertAnswer(1, 123, 1, "t2", a1));
  }

  @Test
  public void testModeratorCanUpdateAnswerOnlyWithRightAccess()
      throws ResourceNotFoundException, WrongCredentialsException {
    ServerPoll pollTemp = ServerPoll.builder()
        .idPoll(ServerPollIdentifier.builder().idPoll(123).build())
        .build();

    ServerQuestionIdentifier iq = ServerQuestionIdentifier.builder()
        .idServerQuestion(1)
        .idxPoll(pollTemp).build();

    ServerQuestionIdentifier iq2 = ServerQuestionIdentifier.builder()
        .idServerQuestion(2)
        .idxPoll(pollTemp).build();

    ServerAnswerIdentifier ia = ServerAnswerIdentifier.builder()
        .idAnswer(1).build();

    ServerAnswer a1 = ServerAnswer.builder()
        .idAnswer(ia)
        .title("Every night :)")
        .build();

    ServerQuestion c1 = ServerQuestion.builder()
        .idServerQuestion(iq)
        .answersToQuestion(Set.of(a1))
        .title("Do you dream of Scorchers ?").build();

    a1.getIdAnswer().setIdxServerQuestion(c1);

    Moderator aloy = Moderator.builder()
        .idModerator(1)
        .username("aloy")
        .secret("chieftain")
        .pollSet(Set.of(pollTemp))
        .build();
    pollTemp = ServerPoll.builder()
        .idPoll(ServerPollIdentifier.builder().idPoll(123)
            .idxModerator(aloy).build())
        .build();

    Moderator ikrie = Moderator.builder()
        .idModerator(2)
        .username("ikrie")
        .secret("banuk")
        .build();

    ClientAnswer a2 = ClientAnswer.builder()
        .title("Just when I destroyed one")
        .build();

    when(modoRepo.findByToken("t1")).thenReturn(Optional.of(aloy));
    when(modoRepo.findByToken("t2")).thenReturn(Optional.of(ikrie));
    when(questionRepository.findById(Mockito.any())).thenReturn(Optional.of(c1));
    when(questionRepository.findById(ServerQuestionIdentifier.builder()
        .idServerQuestion(2).idxPoll(pollTemp).build())).thenReturn(Optional.empty());
    when(pollRepo.findById(Mockito.any())).thenReturn(Optional.empty());
    when(pollRepo.findById(
        ServerPollIdentifier.builder()
            .idPoll(123).idxModerator(aloy).build()
    )).thenReturn(Optional.of(pollTemp));
    when(answerRepository.findById(a1.getIdAnswer())).thenReturn(Optional.of(a1));
    when(answerRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

    //Moderator who owns the poll
    assertEquals(a2.getTitle(),
        ac.updateAnswer(1, 123, 1, 1, "t1", a2).getTitle());
    //wrong params
    assertThrows(ResourceNotFoundException.class, ()-> ac.updateAnswer(1, 123, 2, 1, "t1", a2));
    //Moderator who does not own the poll
    assertThrows(ResourceNotFoundException.class,
        () -> ac.updateAnswer(2, 123, 1, 1, "t2", a2));
    //wrong token
    assertThrows(WrongCredentialsException.class,
        () -> ac.updateAnswer(1, 123, 1, 1, "t2", a2));
  }
}
