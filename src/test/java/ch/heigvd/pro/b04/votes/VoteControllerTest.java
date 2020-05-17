package ch.heigvd.pro.b04.votes;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import ch.heigvd.pro.b04.answers.AnswerController;
import ch.heigvd.pro.b04.answers.AnswerRepository;
import ch.heigvd.pro.b04.answers.ServerAnswer;
import ch.heigvd.pro.b04.answers.ServerAnswerIdentifier;
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
import ch.heigvd.pro.b04.sessions.SessionRepository;
import ch.heigvd.pro.b04.sessions.SessionState;
import ch.heigvd.pro.b04.sessions.exceptions.SessionNotAvailableException;
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

@ExtendWith(MockitoExtension.class)
public class VoteControllerTest {

  @InjectMocks
  VoteController vc;
  @Mock
  ServerPollRepository pollRepo;
  @Mock
  ModeratorRepository modoRepo;
  @Mock
  ParticipantRepository participantRepository;
  @Mock
  QuestionRepository questionRepository;
  @Mock
  SessionRepository sessionRepository;
  @Mock
  AnswerRepository answerRepository;
  @Mock
  ServerVoteRepository voteRepository;

  @Test
  public void newVoteTest() {
    Moderator aloy = Moderator.builder()
        .idModerator(1)
        .username("aloy")
        .secret("chieftain")
        .token("HZD2017")
        .build();

    ServerPollIdentifier identifier = ServerPollIdentifier.builder()
        .idxModerator(aloy)
        .idPoll(123)
        .build();

    ServerSession session = ServerSession.builder().build();
    ServerAnswer a1 = ServerAnswer.builder().build();
    ServerAnswer a2 = ServerAnswer.builder().build();
    ServerQuestion q1 = ServerQuestion.builder().build();
    ServerPoll poll = ServerPoll.builder().build();

    ServerQuestionIdentifier qi1 = ServerQuestionIdentifier.builder()
        .idServerQuestion(1)
        .idxPoll(poll)
        .build();

    ServerAnswerIdentifier identifier1 = ServerAnswerIdentifier.builder()
        .idAnswer(1)
        .idxServerQuestion(q1)
        .build();

    ServerAnswerIdentifier identifier2 = ServerAnswerIdentifier.builder()
        .idAnswer(2)
        .idxServerQuestion(q1)
        .build();

    a1 = ServerAnswer.builder()
        .idAnswer(identifier1)
        .title("Every night :)")
        .build();

    a2 = ServerAnswer.builder()
        .idAnswer(identifier2)
        .title("Just when I destroyed one")
        .build();

    q1 = ServerQuestion.builder()
        .idServerQuestion(qi1)
        .title("Do you dream of Scorchers ?")
        .answersToQuestion(Set.of(a1, a2))
        .build();

    poll = ServerPoll.builder()
        .idPoll(identifier)
        .title("The Scorchers")
        .pollServerQuestions(Set.of(q1))
        .serverSessionSet(Set.of(session))
        .build();

    session = ServerSession.builder()
        .idSession(SessionIdentifier.builder()
            .idSession(1)
            .idxPoll(poll).build())
        .state(SessionState.OPEN).build();

    Participant ikrie = Participant.builder()
        .idParticipant(ParticipantIdentifier.builder()
            .idxServerSession(session)
            .idParticipant(1).build())
        .token("banuk").build();

    // Data access.
    when(modoRepo.findById(1)).thenReturn(Optional.of(aloy));
    lenient().when(modoRepo.findByToken("HZD2017")).thenReturn(Optional.of(aloy));
    lenient().when(pollRepo.findById(identifier)).thenReturn(Optional.of(poll));
    when(pollRepo.findByModeratorAndId(aloy, 123)).thenReturn(Optional.of(poll));
    when(questionRepository.findById(Mockito.any())).thenReturn(Optional.of(q1));
    when(participantRepository.findByToken("banuk")).thenReturn(Optional.of(ikrie));
    when(sessionRepository.findById(session.getIdSession())).thenReturn(Optional.of(session));
    lenient().when(questionRepository.findById(qi1)).thenReturn(Optional.of(q1));
    when(answerRepository.findById(Mockito.any())).thenReturn(Optional.of(a1));
    when(voteRepository.save(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

    assertDoesNotThrow(() -> vc.newVote(1, 123, 1, 1, ikrie.getToken(),
        ClientVote.builder()
            .checked(true).build()));
    //wrong idModerator (we cannot change test wrong idQuestion or idAnswer :()
    assertThrows(ResourceNotFoundException.class, () -> vc.newVote(2, 123, 1, 1, ikrie.getToken(),
        ClientVote.builder()
            .checked(true).build()));
    //wrong token
    assertThrows(WrongCredentialsException.class, () -> vc.newVote(1, 123, 1, 1, "blabla",
        ClientVote.builder()
            .checked(true).build()));
    session.setState(SessionState.QUARANTINED);
    assertDoesNotThrow(() -> vc.newVote(1, 123, 1, 1, ikrie.getToken(),
        ClientVote.builder()
            .checked(true).build()));
    session.setState(SessionState.CLOSED);
    assertThrows(SessionNotAvailableException.class,
        () -> vc.newVote(1, 123, 1, 1, ikrie.getToken(),
            ClientVote.builder()
                .checked(true).build()));
  }
}
