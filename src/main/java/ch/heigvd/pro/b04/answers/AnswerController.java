package ch.heigvd.pro.b04.answers;

import ch.heigvd.pro.b04.auth.exceptions.WrongCredentialsException;
import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.participants.ParticipantRepository;
import ch.heigvd.pro.b04.polls.ServerPoll;
import ch.heigvd.pro.b04.polls.ServerPollIdentifier;
import ch.heigvd.pro.b04.polls.ServerPollRepository;
import ch.heigvd.pro.b04.questions.QuestionRepository;
import ch.heigvd.pro.b04.questions.ServerQuestion;
import ch.heigvd.pro.b04.questions.ServerQuestionIdentifier;
import ch.heigvd.pro.b04.sessions.SessionState;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnswerController {

  private final AnswerRepository repository;
  private final QuestionRepository questionRepository;
  private final ServerPollRepository pollRepository;
  private final ParticipantRepository participantRepository;
  private final ModeratorRepository moderatorRepository;

  public AnswerController(AnswerRepository repository,
      QuestionRepository questionRepository, ServerPollRepository pollRepository,
      ParticipantRepository participantRepository,
      ModeratorRepository moderatorRepository) {
    this.repository = repository;
    this.questionRepository = questionRepository;
    this.pollRepository = pollRepository;
    this.participantRepository = participantRepository;
    this.moderatorRepository = moderatorRepository;
  }

  /**
   * Returns a moderator according its id and token. The 2 have to match.
   *
   * @param idMod The id of the moderator we check access to.
   * @param token The token to check for.
   * @return Optional with the right moderator, empty if not found
   */
  private Optional<Moderator> findVerifiedModeratorByIdAndToken(
      int idMod,
      String token
  ) {
    return moderatorRepository.findByToken(token)
        .filter(moderator -> moderator.getIdModerator() == idMod);
  }

  /**
   * Returns a participant according its id and token. The 2 have to match.
   *
   * @param idMod  The id of the moderator we check access to.
   * @param token  The token to check for.
   * @param idPoll The id of the poll we want to check access to.
   * @return Optional with the right participant, empty if not found
   */
  private Optional<ServerPoll> findVerifiedParticipantByIdAndToken(
      int idMod,
      int idPoll,
      String token
  ) {
    return participantRepository.findByToken(token)
        .map(participant -> participant.getIdParticipant().getIdxServerSession())
        .filter(serverSession -> !(serverSession.getState().equals(SessionState.CLOSED)))
        .map(serverSession -> serverSession.getIdSession().getIdxPoll())
        .filter(poll -> poll.getIdPoll().getIdPoll() == idPoll)
        .filter(poll -> poll.getIdPoll().getIdxModerator().getIdModerator() == idMod);
  }

  private Optional<ServerQuestion> findQuestion(int idModerator, String token, int idPoll,
      int idQuestion)
      throws ResourceNotFoundException {
    Moderator mod = findVerifiedModeratorByIdAndToken(idModerator, token)
        .orElseThrow(ResourceNotFoundException::new);

    ServerPoll poll = pollRepository.findById(ServerPollIdentifier.builder()
        .idxModerator(mod)
        .idPoll(idPoll).build())
        .orElseThrow(ResourceNotFoundException::new);

    Optional<ServerQuestion> question = questionRepository
        .findById(ServerQuestionIdentifier.builder()
            .idServerQuestion(idQuestion).idxPoll(poll).build());

    return question;
  }

  @RequestMapping(value = "/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer", method = RequestMethod.GET)
  public List<ServerAnswer> all(
      @PathVariable(name = "idModerator") int idModerator,
      @PathVariable(name = "idPoll") int idPoll,
      @PathVariable(name = "idQuestion") int idQuestion,
      @RequestParam(name = "token") String token)
      throws WrongCredentialsException, ResourceNotFoundException {
    if ((findVerifiedModeratorByIdAndToken(idModerator, token).isEmpty()
        && findVerifiedParticipantByIdAndToken(idModerator, idPoll, token).isEmpty())) {
      throw new WrongCredentialsException();
    }

    ServerQuestion question = findQuestion(idModerator, token, idPoll, idQuestion)
        .orElseThrow(ResourceNotFoundException::new);

    return repository.findAll().stream()
        .filter(serverAnswer -> serverAnswer.getIdAnswer().getIdxServerQuestion().equals(question))
        .collect(Collectors.toList());
  }

  @RequestMapping(value = "/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}", method = RequestMethod.GET)
  public ServerAnswer byId(
      @PathVariable(name = "idModerator") int idModerator,
      @PathVariable(name = "idPoll") int idPoll,
      @PathVariable(name = "idQuestion") int idQuestion,
      @PathVariable(name = "idAnswer") int idAnswer,
      @RequestParam(name = "token") String token)
      throws WrongCredentialsException, ResourceNotFoundException {
    if ((findVerifiedModeratorByIdAndToken(idModerator, token).isEmpty()
        && findVerifiedParticipantByIdAndToken(idModerator, idPoll, token).isEmpty())) {
      throw new WrongCredentialsException();
    }

    for (ServerAnswer ssA : all(idModerator, idPoll, idQuestion, token)) {
      if (ssA.getIdAnswer().getIdAnswer() == idAnswer) {
        return ssA;
      }
    }

    throw new ResourceNotFoundException("This should never happen. Call Tony Stark !");
  }

  @PostMapping(value = "/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer")
  public ServerAnswer insertAnswer(
      @PathVariable(name = "idModerator") int idModerator,
      @PathVariable(name = "idPoll") int idPoll,
      @PathVariable(name = "idQuestion") int idQuestion,
      @RequestParam(name = "token") String token,
      @RequestBody ClientAnswer answer)
      throws WrongCredentialsException, ResourceNotFoundException {
    ServerQuestion question = findQuestion(idModerator, token, idPoll, idQuestion)
        .orElseThrow(ResourceNotFoundException::new);

    return question.addAnswer(repository, answer);
  }
}
