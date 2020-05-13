package ch.heigvd.pro.b04.answers;

import ch.heigvd.pro.b04.auth.exceptions.WrongCredentialsException;
import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;
import ch.heigvd.pro.b04.messages.ServerMessage;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.participants.Participant;
import ch.heigvd.pro.b04.participants.ParticipantRepository;
import ch.heigvd.pro.b04.polls.ServerPoll;
import ch.heigvd.pro.b04.polls.ServerPollIdentifier;
import ch.heigvd.pro.b04.polls.ServerPollRepository;
import ch.heigvd.pro.b04.questions.QuestionRepository;
import ch.heigvd.pro.b04.questions.ServerQuestion;
import ch.heigvd.pro.b04.questions.ServerQuestionIdentifier;
import ch.heigvd.pro.b04.sessions.SessionState;
import ch.heigvd.pro.b04.votes.ServerVote;
import ch.heigvd.pro.b04.votes.ServerVoteRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnswerController {

  private final AnswerRepository repository;
  private final QuestionRepository questionRepository;
  private final ServerPollRepository pollRepository;
  private final ParticipantRepository participantRepository;
  private final ModeratorRepository moderatorRepository;
  private final ServerVoteRepository voteRepository;

  /**
   * Standard constructor.
   *
   * @param repository            answer repository
   * @param questionRepository    question repository
   * @param pollRepository        poll repository
   * @param participantRepository participant repository
   * @param moderatorRepository   moderator repository
   * @param voteRepository        vote repository
   */
  public AnswerController(AnswerRepository repository,
      QuestionRepository questionRepository, ServerPollRepository pollRepository,
      ParticipantRepository participantRepository,
      ModeratorRepository moderatorRepository,
      ServerVoteRepository voteRepository
  ) {
    this.repository = repository;
    this.questionRepository = questionRepository;
    this.pollRepository = pollRepository;
    this.participantRepository = participantRepository;
    this.moderatorRepository = moderatorRepository;
    this.voteRepository = voteRepository;
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
  private Optional<Participant> findVerifiedParticipantByIdAndToken(
      int idMod,
      int idPoll,
      String token
  ) {
    return participantRepository.findByToken(token)
        .filter(p -> !p.getIdParticipant().getIdxServerSession()
            .getState().equals(SessionState.CLOSED))
        .filter(p -> p.getIdParticipant().getIdxServerSession()
            .getIdSession().getIdxPoll()
            .getIdPoll().getIdPoll() == idPoll)
        .filter(p -> p.getIdParticipant().getIdxServerSession()
            .getIdSession().getIdxPoll()
            .getIdPoll().getIdxModerator()
            .getIdModerator() == idMod);
  }

  /**
   * Returns a function that populates a certain {@link ServerAnswer} with the last user vote,
   * assuming that the provided {@link Participant} is not empty.
   *
   * @param participant An optionally empty participant.
   * @return The mapping function.
   */
  public Function<ServerAnswer, ServerAnswer> populateWithCheckedValues(
      Optional<Participant> participant
  ) {
    return participant.map((Function<Participant, Function<ServerAnswer, ServerAnswer>>) p ->
        serverAnswer -> {
          // If we are connected as a participant.
          serverAnswer.setShowChecked(true);
          // Retrieve the latest vote for the said answer.
          serverAnswer.setChecked(voteRepository.findAll().stream()
              // Fetch the votes for the right participant and answer.
              .filter(vote -> vote.getIdVote().getIdxParticipant() == p)
              .filter(vote -> vote.getIdVote().getIdxServerAnswer() == serverAnswer)
              // Retrieve the last vote.
              .max(Comparator.comparing(serverVote -> serverVote.getIdVote().getTimeVote()))
              // Get its checked status.
              .map(ServerVote::isAnswerChecked)
              // No vote could be found.
              .orElse(false));
          return serverAnswer;
        })
        .orElse(serverAnswer -> {
          // We are not connected as a participant.
          serverAnswer.setShowChecked(false);
          return serverAnswer;
        });
  }

  /**
   * fetch a {@link ServerAnswer} and verify its access by doing it.
   *
   * @param idModerator id of the moderator
   * @param token       token of the moderator
   * @param idPoll      id of the poll owning the question
   * @param idQuestion  id of the question to retrieve
   * @param idAnswer    id of the answer to retrieve
   * @return {@link ServerQuestion} found
   * @throws WrongCredentialsException if token is not a valid moderator token
   */
  private Optional<ServerAnswer> findAnswerByPollAndModerator(
      int idModerator,
      int idPoll,
      int idQuestion,
      int idAnswer,
      String token
  ) throws WrongCredentialsException {
    Moderator moderator = findVerifiedModeratorByIdAndToken(
        idModerator, token).orElseThrow(WrongCredentialsException::new);

    Optional<ServerPoll> poll = pollRepository.findById(ServerPollIdentifier.builder()
        .idxModerator(moderator)
        .idPoll(idPoll)
        .build());

    Optional<ServerQuestion> question = poll
        .flatMap(p -> questionRepository.findById(ServerQuestionIdentifier.builder()
            .idServerQuestion(idQuestion)
            .idxPoll(p)
            .build()));

    return question.flatMap(q -> repository.findById(ServerAnswerIdentifier.builder()
        .idxServerQuestion(q)
        .idAnswer(idAnswer)
        .build()));
  }

  /**
   * Fetch all {@link ServerAnswer} of a {@link ServerQuestion}.
   *
   * @param idModerator id of moderator owning the poll
   * @param idPoll      id of poll owning the question
   * @param idQuestion  id of question owning the answers
   * @param token       accreditation of Participant or Moderator
   * @return List of {@link ServerAnswer}
   * @throws WrongCredentialsException if token is not right
   * @throws ResourceNotFoundException if one of the parameters is broken
   */
  @GetMapping(value = "/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer")
  @Transactional
  public List<ServerAnswer> all(
      @PathVariable(name = "idModerator") int idModerator,
      @PathVariable(name = "idPoll") int idPoll,
      @PathVariable(name = "idQuestion") int idQuestion,
      @RequestParam(name = "token") String token)
      throws WrongCredentialsException, ResourceNotFoundException {

    Optional<Participant> participant =
        findVerifiedParticipantByIdAndToken(idModerator, idPoll, token);

    if ((findVerifiedModeratorByIdAndToken(idModerator, token).isEmpty()
        && participant.isEmpty())) {
      throw new WrongCredentialsException();
    }

    Moderator moderator = moderatorRepository.findById(idModerator)
        .orElseThrow(ResourceNotFoundException::new);

    ServerPoll poll = pollRepository
        .findById(ServerPollIdentifier.builder().idPoll(idPoll).idxModerator(moderator).build())
        .orElseThrow(ResourceNotFoundException::new);

    ServerQuestion question = questionRepository
        .findById(ServerQuestionIdentifier.builder()
            .idServerQuestion(idQuestion)
            .idxPoll(poll)
            .build())
        .orElseThrow(ResourceNotFoundException::new);

    return repository.findAll()
        .stream()
        .filter(answer -> answer.getIdAnswer().getIdxServerQuestion().equals(question))
        .map(populateWithCheckedValues(participant))
        .collect(Collectors.toList());
  }

  /**
   * Fetch a precise {@link ServerAnswer} of a {@link ServerQuestion}.
   *
   * @param idModerator id of moderator owning the poll
   * @param idPoll      id of poll owning the question
   * @param idQuestion  id of question owning the answer
   * @param idAnswer    id of the answer to return
   * @param token       accreditation of Participant or Moderator
   * @return {@link ServerAnswer} found
   * @throws WrongCredentialsException if token is not right
   * @throws ResourceNotFoundException if one of the parameters is broken
   */
  @GetMapping(value = "/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}")
  @Transactional
  public ServerAnswer byId(
      @PathVariable(name = "idModerator") int idModerator,
      @PathVariable(name = "idPoll") int idPoll,
      @PathVariable(name = "idQuestion") int idQuestion,
      @PathVariable(name = "idAnswer") int idAnswer,
      @RequestParam(name = "token") String token)
      throws WrongCredentialsException, ResourceNotFoundException {

    Optional<Participant> participant =
        findVerifiedParticipantByIdAndToken(idModerator, idPoll, token);

    if ((findVerifiedModeratorByIdAndToken(idModerator, token).isEmpty()
        && participant.isEmpty())) {
      throw new WrongCredentialsException();
    }

    Moderator moderator = moderatorRepository.findById(idModerator)
        .orElseThrow(ResourceNotFoundException::new);

    ServerPollIdentifier pollIdentifier = ServerPollIdentifier.builder()
        .idPoll(idPoll)
        .idxModerator(moderator)
        .build();

    ServerPoll poll = pollRepository.findById(pollIdentifier)
        .orElseThrow(ResourceNotFoundException::new);

    ServerQuestionIdentifier questionIdentifier = ServerQuestionIdentifier.builder()
        .idServerQuestion(idQuestion)
        .idxPoll(poll)
        .build();

    ServerQuestion question = questionRepository.findById(questionIdentifier)
        .orElseThrow(ResourceNotFoundException::new);

    ServerAnswerIdentifier answerIdentifier = ServerAnswerIdentifier.builder()
        .idAnswer(idAnswer)
        .idxServerQuestion(question)
        .build();

    return repository.findById(answerIdentifier)
        .map(populateWithCheckedValues(participant))
        .orElseThrow(ResourceNotFoundException::new);
  }

  /**
   * Create in the DB a new {@link ServerAnswer} linked to a {@link ServerQuestion}.
   *
   * @param idModerator id of moderator owning the poll
   * @param idPoll      id of poll owning the question
   * @param idQuestion  id of question owning the answers
   * @param token       accreditation of Participant or Moderator
   * @param answer      {@link ClientAnswer} to insert as a {@link ServerAnswer}
   * @return {@link ServerAnswer} inserted
   * @throws ResourceNotFoundException if one of the parameters is broken
   */
  @PostMapping(value = "/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer")
  public ServerAnswer insertAnswer(
      @PathVariable(name = "idModerator") int idModerator,
      @PathVariable(name = "idPoll") int idPoll,
      @PathVariable(name = "idQuestion") int idQuestion,
      @RequestParam(name = "token") String token,
      @RequestBody ClientAnswer answer)
      throws ResourceNotFoundException, WrongCredentialsException {

    Moderator moderator = findVerifiedModeratorByIdAndToken(idModerator, token)
        .orElseThrow(WrongCredentialsException::new);

    // Retrieve the associated poll.
    ServerPoll poll = pollRepository.findById(ServerPollIdentifier.builder()
        .idxModerator(moderator)
        .idPoll(idPoll)
        .build())
        .orElseThrow(ResourceNotFoundException::new);

    // Retrieve the associated question.
    ServerQuestion question = questionRepository.findById(ServerQuestionIdentifier.builder()
        .idxPoll(poll)
        .idServerQuestion(idQuestion)
        .build())
        .orElseThrow(ResourceNotFoundException::new);

    return question.newAnswer(repository, answer);
  }

  /**
   * Modify a precise {@link ServerAnswer} according a new {@link ClientAnswer}.
   *
   * @param idModerator id of moderator owning the poll
   * @param idPoll      id of poll owning the question
   * @param idQuestion  id of question owning the answer
   * @param idAnswer    id of the answer to return
   * @param token       accreditation of Participant or Moderator
   * @param answer      new {@link ClientAnswer} model
   * @return {@link ServerAnswer} updated
   * @throws ResourceNotFoundException if one of the parameters is broken
   */
  @PutMapping(value = "/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}")
  @Transactional
  public ServerAnswer updateAnswer(
      @PathVariable(name = "idModerator") int idModerator,
      @PathVariable(name = "idPoll") int idPoll,
      @PathVariable(name = "idQuestion") int idQuestion,
      @PathVariable(name = "idAnswer") int idAnswer,
      @RequestParam(name = "token") String token,
      @RequestBody ClientAnswer answer)
      throws ResourceNotFoundException, WrongCredentialsException {

    ServerAnswer toUpdate =
        findAnswerByPollAndModerator(
            idModerator,
            idPoll,
            idQuestion,
            idAnswer,
            token
        ).orElseThrow(ResourceNotFoundException::new);

    toUpdate.setTitle(answer.getTitle());
    toUpdate.setDescription(answer.getDescription());
    return toUpdate;
  }

  /**
   * Delete a precise {@link ServerAnswer}.
   *
   * @param idModerator id of moderator owning the poll
   * @param idPoll      id of poll owning the question
   * @param idQuestion  id of question owning the answer
   * @param idAnswer    id of the answer to delete
   * @param token       accreditation of Participant or Moderator
   * @return {@link ServerMessage} confirming suppression
   * @throws WrongCredentialsException if token is not right
   * @throws ResourceNotFoundException if one of the parameters is broken
   */
  @DeleteMapping(value = "/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}")
  public ServerMessage deleteAnswer(
      @PathVariable(name = "idModerator") int idModerator,
      @PathVariable(name = "idPoll") int idPoll,
      @PathVariable(name = "idQuestion") int idQuestion,
      @PathVariable(name = "idAnswer") int idAnswer,
      @RequestParam(name = "token") String token)
      throws ResourceNotFoundException, WrongCredentialsException {
    ServerAnswer answer =
        findAnswerByPollAndModerator(
            idModerator,
            idPoll,
            idQuestion,
            idAnswer,
            token
        ).orElseThrow(ResourceNotFoundException::new);
    repository.delete(answer);
    return ServerMessage.builder().message("Answer deleted").build();
  }
}
