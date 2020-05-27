package ch.heigvd.pro.b04.votes;

import ch.heigvd.pro.b04.answers.AnswerRepository;
import ch.heigvd.pro.b04.answers.ServerAnswer;
import ch.heigvd.pro.b04.answers.ServerAnswerIdentifier;
import ch.heigvd.pro.b04.auth.exceptions.WrongCredentialsException;
import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.participants.Participant;
import ch.heigvd.pro.b04.participants.ParticipantRepository;
import ch.heigvd.pro.b04.polls.ServerPollRepository;
import ch.heigvd.pro.b04.questions.QuestionRepository;
import ch.heigvd.pro.b04.questions.QuestionVisibility;
import ch.heigvd.pro.b04.questions.ServerQuestion;
import ch.heigvd.pro.b04.questions.ServerQuestionIdentifier;
import ch.heigvd.pro.b04.sessions.ServerSession;
import ch.heigvd.pro.b04.sessions.SessionRepository;
import ch.heigvd.pro.b04.sessions.SessionState;
import ch.heigvd.pro.b04.sessions.exceptions.SessionNotAvailableException;
import ch.heigvd.pro.b04.sessions.exceptions.SessionNotExistingException;
import javax.transaction.Transactional;
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
  private final ServerVoteRepository voteRepository;
  private final AnswerRepository answerRepository;
  private final SessionRepository sessionRepository;
  private final ServerPollRepository pollRepository;

  /**
   * Standard constructor.
   *
   * @param moderatorRepository   {@link Moderator} repository
   * @param participantRepository {@link Participant} repository
   * @param questionRepository    {@link ServerQuestion} repository
   * @param voteRepository        {@link ServerVote} repository
   * @param answerRepository      {@link ServerAnswer} repository
   * @param sessionRepository     {@link ServerSession} repository
   * @param pollRepository        {@link ch.heigvd.pro.b04.polls.ServerPoll} repository
   */
  public VoteController(ModeratorRepository moderatorRepository,
      ParticipantRepository participantRepository,
      QuestionRepository questionRepository,
      ServerVoteRepository voteRepository,
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

  /**
   * Create a new vote in database.
   *
   * @param idModerator {@link Moderator} owning the poll
   * @param idPoll      {@link ch.heigvd.pro.b04.polls.ServerPoll} owning the question
   * @param idQuestion  {@link ServerQuestion} owning the answer
   * @param idAnswer    {@link ServerAnswer} answer which Participant voted for
   * @param token       token of Participant (/!\ Participant only)
   * @param vote        boolean if answer has been checked or not
   * @return new {@link ServerVote} created
   * @throws ResourceNotFoundException    if one of the parameters is broken
   * @throws SessionNotAvailableException if Participant cannot vote now
   * @throws WrongCredentialsException    if token is not right
   */
  @PutMapping(value = "/mod/{idModerator}/poll/{idPoll}"
      + "/question/{idQuestion}/answer/{idAnswer}/vote")
  @Transactional
  public ServerVote newVote(
      @PathVariable(name = "idModerator") int idModerator,
      @PathVariable(name = "idPoll") int idPoll,
      @PathVariable(name = "idQuestion") int idQuestion,
      @PathVariable(name = "idAnswer") int idAnswer,
      @RequestParam(name = "token") String token,
      @RequestBody ClientVote vote)
      throws ResourceNotFoundException, SessionNotAvailableException, WrongCredentialsException {
    Participant voter = participantRepository.findByToken(token)
        .orElseThrow(WrongCredentialsException::new);

    ServerSession session = sessionRepository.findById(
        voter.getIdParticipant().getIdxServerSession().getIdSession())
        .orElseThrow(SessionNotExistingException::new);
    if (session.getState() == SessionState.CLOSED) {
      throw new SessionNotAvailableException();
    }

    ServerQuestion question = questionRepository
        .findById(ServerQuestionIdentifier.builder()
            .idServerQuestion(idQuestion)
            .idxPoll(session.getIdSession().getIdxPoll())
            .build())
        .filter(serverQuestion -> serverQuestion.getVisibility() == QuestionVisibility.VISIBLE)
        .orElseThrow(ResourceNotFoundException::new);

    ServerAnswer answerChanged = answerRepository
        .findById(ServerAnswerIdentifier.builder()
            .idAnswer(idAnswer)
            .idxServerQuestion(question)
            .build())
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

    ServerVote newVote = ServerVote.builder()
        .idVote(VoteIdentifier.builder()
            .idxParticipant(voter)
            .idxServerAnswer(answerChanged)
            .build())
        .answerChecked(vote.isChecked())
        .build();

    return voteRepository.save(newVote);
  }
}
