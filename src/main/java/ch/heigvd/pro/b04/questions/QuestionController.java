package ch.heigvd.pro.b04.questions;

import ch.heigvd.pro.b04.answers.ServerAnswer;
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
import ch.heigvd.pro.b04.sessions.SessionState;
import ch.heigvd.pro.b04.sessions.exceptions.IndexInPollAlreadyExist;
import java.util.List;
import java.util.Optional;
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
public class QuestionController {

  private final QuestionRepository repository;
  private final ServerPollRepository pollRepository;
  private final ParticipantRepository participantRepository;
  private final ModeratorRepository moderatorRepository;

  /**
   * Standard constructor.
   *
   * @param repository            repository for {@link ServerQuestion}
   * @param pollRepository        repository for {@link ServerPoll}
   * @param participantRepository repository for {@link Participant}
   * @param moderatorRepository   repository for {@link Moderator}
   */
  public QuestionController(QuestionRepository repository,
      ServerPollRepository pollRepository,
      ParticipantRepository participantRepository,
      ModeratorRepository moderatorRepository) {
    this.repository = repository;
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

  /**
   * fetch a {@link ServerQuestion} and verify its access by doing it.
   *
   * @param idModerator id of the moderator
   * @param token       token of the moderator
   * @param idPoll      id of the poll owning the question
   * @param idQuestion  id of the question to retrieve
   * @return {@link ServerQuestion} found
   * @throws WrongCredentialsException if token is not a valid moderator token
   */
  private Optional<ServerQuestion> findQuestionByPollAndModerator(
      int idModerator,
      int idPoll,
      int idQuestion,
      String token
  ) throws WrongCredentialsException, IndexInPollAlreadyExist {
    Moderator moderator = findVerifiedModeratorByIdAndToken(
        idModerator, token).orElseThrow(WrongCredentialsException::new);

    Optional<ServerPoll> poll = pollRepository.findById(ServerPollIdentifier.builder()
        .idxModerator(moderator)
        .idPoll(idPoll)
        .build());

    return poll.flatMap(p -> repository.findById(ServerQuestionIdentifier.builder()
        .idServerQuestion(idQuestion)
        .idxPoll(p)
        .build()));
  }

  private void verifyAndInsertIndexInPoll(ServerPoll poll, double index)
      throws IndexInPollAlreadyExist {
    if (poll.getAllIndexs().contains(index)) {
      throw new IndexInPollAlreadyExist();
    }
    poll.getAllIndexs().add(index);
  }

  /**
   * return all {@link ServerQuestion} of a {@link ServerPoll}.
   *
   * @param idModerator id of moderator owning the poll
   * @param token       token of the sender, moderator or participant
   * @param idPoll      id of the poll containing question
   * @return list of {@link ServerQuestion}
   * @throws ResourceNotFoundException if sender or poll is not found
   * @throws WrongCredentialsException if the sender cannot access this poll
   */
  @GetMapping(value = "/mod/{idModerator}/poll/{idPoll}/question")
  @Transactional
  public List<ServerQuestion> all(
      @PathVariable(name = "idModerator") int idModerator,
      @PathVariable(name = "idPoll") int idPoll,
      @RequestParam(name = "token") String token
  ) throws ResourceNotFoundException, WrongCredentialsException {

    boolean verifiedAsModerator = findVerifiedModeratorByIdAndToken(idModerator, token).isPresent();
    boolean verifiedAsParticipant =
        findVerifiedParticipantByIdAndToken(idModerator, idPoll, token).isPresent();

    if (!verifiedAsModerator && !verifiedAsParticipant) {
      throw new WrongCredentialsException();
    }

    Moderator moderator = moderatorRepository.findById(idModerator)
        .orElseThrow(ResourceNotFoundException::new);

    ServerPoll poll = pollRepository
        .findById(ServerPollIdentifier.builder().idPoll(idPoll).idxModerator(moderator).build())
        .orElseThrow(ResourceNotFoundException::new);

    return repository.findAll()
        .stream()
        .filter(question -> question.getIdServerQuestion().getIdxPoll().equals(poll))
        .filter(question ->
            verifiedAsModerator || question.getVisibility() == QuestionVisibility.VISIBLE)
        .collect(Collectors.toList());
  }

  /**
   * gets a {@link ServerQuestion} by his id.
   *
   * @param idModerator id of moderator owning the poll
   * @param token       token of the moderator or participant
   * @param idPoll      part of the {@link ServerPollIdentifier}
   * @param idQuestion  part of the {@link ServerQuestionIdentifier}
   * @return {@link ServerQuestion} found
   * @throws ResourceNotFoundException if one of the parameters is broken
   * @throws WrongCredentialsException if there a credentials violation
   */
  @GetMapping(value = "/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}")
  @Transactional
  public ServerQuestion byId(
      @PathVariable(name = "idModerator") int idModerator,
      @PathVariable(name = "idPoll") int idPoll,
      @PathVariable(name = "idQuestion") int idQuestion,
      @RequestParam(name = "token") String token
  ) throws ResourceNotFoundException, WrongCredentialsException {

    if ((findVerifiedModeratorByIdAndToken(idModerator, token).isEmpty()
        && findVerifiedParticipantByIdAndToken(idModerator, idPoll, token).isEmpty())) {
      throw new WrongCredentialsException();
    }

    ServerQuestionIdentifier idToFind = ServerQuestionIdentifier.builder()
        .idServerQuestion(idQuestion)
        .idxPoll(pollRepository.findById(
            ServerPollIdentifier.builder()
                .idPoll(idPoll)
                .idxModerator(moderatorRepository.findById(idModerator)
                    .orElseThrow(ResourceNotFoundException::new))
                .build())
            .orElseThrow(ResourceNotFoundException::new))
        .build();

    return repository.findById(idToFind)
        .orElseThrow(ResourceNotFoundException::new);
  }

  /**
   * Insert a new {@link ServerQuestion} in a {@link ServerPoll}.
   *
   * @param idModerator moderator who should be the sender of the request
   * @param token       sender's token
   * @param idPoll      poll to add question in
   * @param question    question to add
   * @return question added
   * @throws ResourceNotFoundException if one parameter is broken
   * @throws WrongCredentialsException if there is a credentials problem
   */
  @PostMapping(value = "/mod/{idModerator}/poll/{idPoll}/question")
  @Transactional
  public ServerQuestion insert(
      @PathVariable(name = "idModerator") int idModerator,
      @PathVariable(name = "idPoll") int idPoll,
      @RequestParam(name = "token") String token,
      @RequestBody ClientQuestion question
  ) throws ResourceNotFoundException, WrongCredentialsException {

    Moderator moderator = findVerifiedModeratorByIdAndToken(idModerator, token)
        .orElseThrow(WrongCredentialsException::new);

    // Retrieve the associated poll.
    ServerPoll poll = pollRepository.findById(ServerPollIdentifier.builder()
        .idxModerator(moderator)
        .idPoll(idPoll)
        .build())
        .orElseThrow(ResourceNotFoundException::new);
    verifyAndInsertIndexInPoll(poll, question.getIndexInPoll());
    return poll.newQuestion(repository, question);
  }

  /**
   * Update a {@link ServerQuestion} in a {@link ServerPoll}.
   *
   * @param idModerator id of the moderator
   * @param token       token of the moderator
   * @param idPoll      id of the poll owning the question
   * @param idQuestion  id of the question to modify
   * @param question    new representation of the question
   * @return {@link ServerQuestion} modified
   * @throws WrongCredentialsException if one of the parameters is broken
   * @throws ResourceNotFoundException if one of the parameters is broken
   */
  @PutMapping(value = "/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}")
  @Transactional
  public ServerQuestion updateQuestion(
      @PathVariable(name = "idModerator") int idModerator,
      @PathVariable(name = "idPoll") int idPoll,
      @PathVariable(name = "idQuestion") int idQuestion,
      @RequestParam(name = "token") String token,
      @RequestBody ClientQuestion question
  ) throws WrongCredentialsException, ResourceNotFoundException {

    ServerQuestion updated =
        findQuestionByPollAndModerator(idModerator, idPoll, idQuestion, token)
            .orElseThrow(ResourceNotFoundException::new);

    if (updated.indexInPoll != question.getIndexInPoll()) {
      verifyAndInsertIndexInPoll(updated.getIdServerQuestion().getIdxPoll(),
          question.getIndexInPoll());
      updated.getIdServerQuestion().getIdxPoll().getAllIndexs().remove(question.getIndexInPoll());
    }

    updated.setIndexInPoll(question.getIndexInPoll());
    updated.setTitle(question.getTitle());
    updated.setDetails(question.getDetails());
    updated.setVisibility(question.getVisibility());
    updated.setAnswersMin(question.answersMin);
    updated.setAnswersMax(question.answersMax);

    return updated;
  }

  /**
   * Update a {@link ServerQuestion} in a {@link ServerPoll}.
   *
   * @param idModerator id of the moderator
   * @param token       token of the moderator
   * @param idPoll      id of the poll owning the question
   * @param idQuestion  id of the question to delete
   * @return confirmation message
   * @throws ResourceNotFoundException if one of the parameters is broken
   */
  @DeleteMapping(value = "/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}")
  @Transactional
  public ServerMessage deleteQuestion(
      @PathVariable(name = "idModerator") int idModerator,
      @PathVariable(name = "idPoll") int idPoll,
      @PathVariable(name = "idQuestion") int idQuestion,
      @RequestParam(name = "token") String token)
      throws ResourceNotFoundException, WrongCredentialsException {

    ServerQuestion question =
        findQuestionByPollAndModerator(idModerator, idPoll, idQuestion, token)
            .orElseThrow(ResourceNotFoundException::new);

    repository.delete(question);
    return ServerMessage.builder().message("Question deleted").build();
  }
}
