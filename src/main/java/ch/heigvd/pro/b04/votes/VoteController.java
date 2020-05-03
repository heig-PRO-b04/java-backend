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
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoteController {

  private final ModeratorRepository moderatorRepository;
  private final ParticipantRepository participantRepository;
  private final QuestionRepository questionRepository;
  private final VoteRepository voteRepository;
  private final AnswerRepository answerRepository;
  private final SessionRepository sessionRepository;
  private final ServerPollRepository pollRepository;

  public VoteController(ModeratorRepository moderatorRepository,
      ParticipantRepository participantRepository,
      QuestionRepository questionRepository, VoteRepository voteRepository,
      AnswerRepository answerRepository,
      SessionRepository sessionRepository,
      ServerPollRepository pollRepository) {
    this.moderatorRepository = moderatorRepository;
    this.participantRepository = participantRepository;
    this.questionRepository = questionRepository;
    this.voteRepository = voteRepository;
    this.answerRepository = answerRepository;
    this.sessionRepository = sessionRepository;
    this.pollRepository = pollRepository;
  }

  @PutMapping(value = "/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}/vote")
  public Vote newVote(
      @PathVariable(name = "idModerator") int idModerator,
      @PathVariable(name = "idPoll") int idPoll,
      @PathVariable(name = "idQuestion") int idQuestion,
      @PathVariable(name = "idAnswer") int idAnswer,
      @RequestParam(name = "token") String token,
      @RequestBody BooleanVote vote)
      throws ResourceNotFoundException, SessionNotAvailableException, WrongCredentialsException {
    System.out.println("entered");
    Participant voter = participantRepository.findByToken(token)
        .orElseThrow(WrongCredentialsException::new);
    System.out.println("token good");
    ServerSession session = sessionRepository.findById(
        voter.getIdParticipant().getIdxServerSession().getIdSession())
        .orElseThrow(SessionNotExistingException::new);
    if (session.getState() == SessionState.CLOSED) {
      throw new SessionNotAvailableException();
    }

    ServerQuestion question = questionRepository
        .findById(ServerQuestionIdentifier.builder()
            .idServerQuestion(idQuestion).idxPoll(session.getIdSession().getIdxPoll()).build())
        .orElseThrow(ResourceNotFoundException::new);

    ServerAnswer answerChanged = answerRepository.findById(ServerAnswerIdentifier.builder()
        .idAnswer(idAnswer).idxServerQuestion(question).build())
        .orElseThrow(ResourceNotFoundException::new);

    //compare session accessed by Participant to Session accessed by Poll
    if ((!(session.getIdSession().getIdxPoll().equals(pollRepository.findByModeratorAndId(
        (moderatorRepository.findById(idModerator).orElseThrow(ResourceNotFoundException::new)),
        idPoll).orElseThrow(ResourceNotFoundException::new))))
        //verify this session contains the wanted question
        || (!(session.getIdSession().getIdxPoll().getPollServerQuestions().contains(question)))
        //verify this question contains the wanted answer
        || (!(question.getAnswersToQuestion().contains(answerChanged)))) {
      throw new ResourceNotFoundException();
    }
    
    Vote newVote = Vote.builder()
        .idVote(VoteIdentifier.builder()
            .idxParticipant(voter)
            .idxServerAnswer(answerChanged)
            .build())
        .answerChecked(vote.isChecked())
        .build();

    return voteRepository.save(newVote);
  }
}
