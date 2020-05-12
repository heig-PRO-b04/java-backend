package ch.heigvd.pro.b04.answers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import ch.heigvd.pro.b04.auth.exceptions.WrongCredentialsException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.participants.ParticipantRepository;
import ch.heigvd.pro.b04.polls.ServerPoll;
import ch.heigvd.pro.b04.polls.ServerPollIdentifier;
import ch.heigvd.pro.b04.polls.ServerPollRepository;
import ch.heigvd.pro.b04.questions.QuestionRepository;
import ch.heigvd.pro.b04.questions.ServerQuestion;
import ch.heigvd.pro.b04.questions.ServerQuestionIdentifier;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * All those tests are very similar to Question tests
 * because Answers behave the same
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
    when(modoRepo.findByToken("banuk")).thenReturn(Optional.of(ikrie));
    lenient().when(pollRepo.findById(identifier)).thenReturn(Optional.of(pollTemp));
    lenient().when(questionRepository.findById(qi1)).thenReturn(Optional.of(q1));

    assertThrows(WrongCredentialsException.class, () -> ac.all(1, 123, 1,"banuk"));
  }

  @Test
  public void testAllEndpointsReturnAllAnswers() {

    Moderator aloy = Moderator.builder()
        .idModerator(1)
        .username("aloy")
        .token("chieftain")
        .build();

    ServerPollIdentifier identifier = ServerPollIdentifier.builder()
        .idxModerator(aloy)
        .idPoll(123)
        .build();

    ServerPoll poll = ServerPoll.builder()
        .idPoll(identifier)
        .build();

    ServerQuestionIdentifier qi1 = ServerQuestionIdentifier.builder()
        .idServerQuestion(1)
        .idxPoll(poll)
        .build();

    ServerQuestion q1 = ServerQuestion.builder()
        .idServerQuestion(qi1)
        .title("Do you dream of Scorchers ?")
        .build();

    ServerAnswerIdentifier identifier1=ServerAnswerIdentifier.builder()
        .idAnswer(1)
        .idxServerQuestion(q1)
        .build();

    ServerAnswerIdentifier identifier2=ServerAnswerIdentifier.builder()
        .idAnswer(2)
        .idxServerQuestion(q1)
        .build();

    ServerAnswer a1=ServerAnswer.builder()
        .idAnswer(identifier1)
        .title("Every night :)")
        .build();

    ServerAnswer a2=ServerAnswer.builder()
        .idAnswer(identifier2)
        .title("Only when they nearly killed me")
        .build();

    // Data access.
    when(modoRepo.findById(1)).thenReturn(Optional.of(aloy));
    when(pollRepo.findById(identifier)).thenReturn(Optional.of(poll));
    when(questionRepository.findById(qi1)).thenReturn(Optional.of(q1));
    lenient().when(answerRepository.findAll())
        .thenReturn(List.of(a1,a2));

    // Token management.
    lenient().when(modoRepo.findByToken("chieftain")).thenReturn(Optional.of(aloy));
    lenient().when(participantRepository.findByToken("chieftain")).thenReturn(Optional.empty());

    assertDoesNotThrow(() -> assertEquals(List.of(a1, a2), ac.all(1, 123,1, "chieftain")));
  }
}
