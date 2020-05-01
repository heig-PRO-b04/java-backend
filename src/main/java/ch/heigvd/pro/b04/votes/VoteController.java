package ch.heigvd.pro.b04.votes;

import ch.heigvd.pro.b04.answers.AnswerRepository;
import ch.heigvd.pro.b04.answers.ServerAnswer;
import ch.heigvd.pro.b04.answers.ServerAnswerIdentifier;
import ch.heigvd.pro.b04.auth.exceptions.WrongCredentialsException;
import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.participants.Participant;
import ch.heigvd.pro.b04.participants.ParticipantRepository;
import ch.heigvd.pro.b04.polls.ServerPoll;
import ch.heigvd.pro.b04.polls.ServerPollRepository;
import ch.heigvd.pro.b04.questions.QuestionRepository;
import ch.heigvd.pro.b04.questions.ServerQuestion;
import ch.heigvd.pro.b04.questions.ServerQuestionIdentifier;
import ch.heigvd.pro.b04.sessions.ServerSession;
import ch.heigvd.pro.b04.sessions.SessionRepository;
import ch.heigvd.pro.b04.sessions.SessionState;
import ch.heigvd.pro.b04.sessions.exceptions.SessionNotAvailableException;
import ch.heigvd.pro.b04.sessions.exceptions.SessionNotExistingException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
public class VoteController {

  private final ModeratorRepository moderatorRepository;
  private final ParticipantRepository participantRepository;
  private final QuestionRepository questionRepository;
  private final VoteRepository voteRepository;
  private final AnswerRepository answerRepository;
  private final SessionRepository sessionRepository;
  private final ServerPollRepository pollRepository;


  @PutMapping(value = "/mod/{idModerator}/poll/{idPoll}"
      + "/question/{idQuestion}/answer/{idAnswer}/vote")
  public void newVote(@RequestParam(name = "token") String token,
      @RequestBody boolean checked,
      @PathVariable(name = "idModerator") int idModerator,
      @PathVariable(name = "idPoll") int idPoll,
      @PathVariable(name = "idQuestion") int idQuestion,
      @PathVariable(name = "idAnswer") int idAnswer)
      throws ResourceNotFoundException, SessionNotAvailableException, SessionNotExistingException, WrongCredentialsException {

    Participant voter = participantRepository.findByToken(token)
        .orElseThrow(ResourceNotFoundException::new);

    ServerSession session = sessionRepository.findById(
        voter.getIdParticipant().getIdxServerSession().getIdSession())
        .orElseThrow(SessionNotExistingException::new);
    if (session.getState() == SessionState.CLOSED) {
      throw new SessionNotAvailableException();
    }

    if (!(session.equals(voter.getIdParticipant().getIdxServerSession()))) {
      throw new WrongCredentialsException();
    }

    ServerQuestion question=questionRepository
        .findById(ServerQuestionIdentifier.builder()
            .idServerQuestion(idQuestion).idxPoll(session.getIdSession().getIdxPoll()).build())
        .orElseThrow(ResourceNotFoundException::new);

    ServerAnswer answerChanged = answerRepository.findById(ServerAnswerIdentifier.builder()
        .idAnswer(idAnswer).idxServerQuestion(question).build())
    .orElseThrow(ResourceNotFoundException::new);

    if(!(session.getIdSession().getIdxPoll().equals(pollRepository.findByModeratorAndId(
        moderatorRepository.findById(idModerator).orElseThrow(ResourceNotFoundException::new),idPoll)
    )) || !(session.getIdSession().getIdxPoll().getPollServerQuestions().contains(question))
        || !(question.getAnswersToQuestion().contains(answerChanged))
    ){
      throw new ResourceNotFoundException();
    }

//
//
//    if (voter.isEmpty() || answerChanged.isEmpty()) {
//      throw new ResourceNotFoundException();
//    }

    Vote newVote = Vote.builder()
        .idVote(VoteIdentifier.builder()
            .idxParticipant(voter)
            .idxServerAnswer(answerChanged)
            .build())
        .answerChecked(checked)
        .build();

    voteRepository.saveAndFlush(newVote);
  }
}
