package ch.heigvd.pro.b04.questions;

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
import ch.heigvd.pro.b04.polls.exceptions.PollNotExistingException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
   * return all {@link ServerQuestion} of a {@link ServerPoll}.
   *
   * @param idPoll id of the poll containing question
   * @param token  token of the sender, moderator or participant
   * @param idModo id of moderator owning the poll
   * @return list of {@link ServerQuestion}
   * @throws ResourceNotFoundException if sender or poll is not found
   * @throws WrongCredentialsException if the sender cannot access this poll
   */
  @RequestMapping(value = "/mod/{idModerator}/poll/{idPoll}/question", method = RequestMethod.GET)
  public List<ServerQuestion> all(@PathVariable(name = "idPoll") ServerPollIdentifier idPoll,
      @RequestParam(name = "token") String token,
      @PathVariable(name = "idModerator") int idModo)
      throws ResourceNotFoundException, WrongCredentialsException, PollNotExistingException {

    ServerPoll pollTest = verifyModeratorOrParticipantAccess(
        participantRepository, moderatorRepository, idPoll, token);

    Optional<ServerPoll> pollConcerned = pollRepository.findById(idPoll);

    if (pollConcerned.isEmpty()) {
      throw new ResourceNotFoundException();
    } else if (!(pollTest.equals(pollConcerned.get()))) {
      throw new WrongCredentialsException();
    }

    return pollConcerned.get().getPollServerQuestions().stream().collect(Collectors.toList());
  }

  /**
   * return a chosen {@link ServerQuestion} of a {@link ServerPoll}.
   *
   * @param idPoll     id of the poll containing question
   * @param token      token of the sender, moderator or participant
   * @param idQuestion id of the Question to return
   * @return {@link ServerQuestion} wanted
   * @throws ResourceNotFoundException if sender or question or poll is not found
   * @throws WrongCredentialsException if the sender cannot access this poll
   */
  @GetMapping(value = "/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}")
  public ServerQuestion byId(@RequestParam(name = "token") String token,
      @PathVariable(name = "idPoll") ServerPollIdentifier idPoll,
      @PathVariable(name = "idQuestion") ServerQuestionIdentifier idQuestion)
      throws ResourceNotFoundException, WrongCredentialsException, PollNotExistingException {
    ServerPoll pollTest = verifyModeratorOrParticipantAccess(participantRepository,
        moderatorRepository, idPoll, token);

    Optional<ServerPoll> pollConcerned = pollRepository.findById(idPoll);
    if (pollConcerned.isEmpty()) {
      throw new ResourceNotFoundException();
    } else if (!(pollTest.equals(pollConcerned.get()))) {
      throw new WrongCredentialsException();
    }

    return repository.findById(idQuestion).orElseThrow(ResourceNotFoundException::new);
  }

  private ServerPoll verifyModeratorOrParticipantAccess(ParticipantRepository prpRepo,
      ModeratorRepository modoRepo, ServerPollIdentifier idPoll, String token)
      throws ResourceNotFoundException, PollNotExistingException {
    ServerPoll pollTest;
    Optional<Participant> pollT = prpRepo.findByToken(token);
    //if token doesn't lead to a Participant...
    if (pollT.isEmpty()) {
      //...it looks for a moderator
      Optional<Moderator> pollM = modoRepo.findByToken(token);
      if (pollM.isEmpty()) {
        throw new ResourceNotFoundException();
      } else {
        //poll inside pollSet of the modo
        pollTest = pollM.get().searchPoll(idPoll);
      }
    } else {
      //poll of the session in which Participant is logged
      pollTest = pollT.get().getIdParticipant()
          .getIdxServerSession().getIdSession().getIdxPoll();
    }
    //If a participant or a moderator with this token exists,
    //the poll corresponding to idPoll is returned
    return pollTest;
  }

  /**
   * Insert a new {@link ServerQuestion} in a {@link ServerPoll}.
   *
   * @param token       sender's token
   * @param question    question to add
   * @param idModerator moderator who should be the sender of the request
   * @param idPoll      poll to add question in
   * @return question added
   * @throws ResourceNotFoundException if one parameter is broken
   * @throws WrongCredentialsException if there is a credentials problem
   */
  @PostMapping(value = "/mod/{idModerator}/poll/{idPoll}/question")
  public ServerQuestion insertQuestion(@RequestParam(name = "token") String token,
      @RequestBody ClientQuestion question,
      @PathVariable(name = "idModerator") int idModerator,
      @PathVariable(name = "idPoll") ServerPollIdentifier idPoll)
      throws ResourceNotFoundException, WrongCredentialsException {

    controlModeratorAccessToPoll(idModerator, idPoll, token);

    return pollRepository.findById(idPoll).get().newQuestion(repository, question);
  }

  /**
   * Update a {@link ServerQuestion} in a {@link ServerPoll}.
   *
   * @param token    sender's token
   * @param question new version of the question
   * @param idModo   moderator who should be the sender of the request
   * @param idPoll   poll to add question in
   * @param maggieQ  id of question to update
   * @throws ResourceNotFoundException if one parameter is broken
   * @throws WrongCredentialsException if there is a credentials problem
   */
  @PutMapping(value = "/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}")
  public void updateQuestion(@RequestParam(name = "token") String token,
      @RequestBody ClientQuestion question,
      @PathVariable(name = "idModerator") int idModo,
      @PathVariable(name = "idPoll") ServerPollIdentifier idPoll,
      @PathVariable(name = "idQuestion") ServerQuestionIdentifier maggieQ)
      throws ResourceNotFoundException, WrongCredentialsException {
    controlModeratorAccessToPoll(idModo, idPoll, token);
    ServerPoll pollTest = pollRepository.findById(idPoll).get();
    Optional<ServerQuestion> upQ = repository.findById(maggieQ);
    if (upQ.isEmpty() || !(pollTest.equals(maggieQ.getIdxPoll()))) {
      throw new ResourceNotFoundException();
    }

    upQ.get().setTitle(question.getTitle());
    upQ.get().setDetails(question.getDetails());
    upQ.get().setAnswersMax(question.getAnswersMax());
    upQ.get().setAnswersMin(question.getAnswersMin());
    upQ.get().setVisibility(question.getVisibility());
    upQ.get().setIndexInPoll(question.getIndexInPoll());
  }

  /**
   * Delete a {@link ServerQuestion} in a {@link ServerPoll}.
   *
   * @param token   sender's token
   * @param idModo  moderator who should be the sender of the request
   * @param idPoll  poll to delete question in
   * @param maggieQ id of question to delete
   * @return message to confirm deletion succeeded
   * @throws ResourceNotFoundException if one parameter is broken
   */
  @DeleteMapping(value = "/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}")
  public ServerMessage deleteQuestion(@RequestParam(name = "token") String token,
      @PathVariable(name = "idModerator") int idModo,
      @PathVariable(name = "idPoll") ServerPollIdentifier idPoll,
      @PathVariable(name = "idQuestion") ServerQuestionIdentifier maggieQ)
      throws ResourceNotFoundException, WrongCredentialsException {
    controlModeratorAccessToPoll(idModo, idPoll, token);

    ServerPoll pollTest = pollRepository.findById(idPoll).get();
    Optional<ServerQuestion> upQ = repository.findById(maggieQ);
    if (upQ.isEmpty() || !(pollTest.equals(maggieQ.getIdxPoll()))) {
      throw new ResourceNotFoundException();
    }

    repository.delete(upQ.get());
    return ServerMessage.builder().message("Question deleted").build();
  }

  /**
   * Test if a token belongs to a {@link Moderator}, and if a {@link ServerPoll} belongs to this
   * Moderator.
   *
   * @param idModerator id of the modo to test
   * @param idPoll      id of the poll
   * @param token       token of the moderator
   * @return true it test is correct
   * @throws ResourceNotFoundException if one of the parameters is not found
   * @throws WrongCredentialsException if the tests fails, there is a credentials problem
   */
  private boolean controlModeratorAccessToPoll(int idModerator,
      ServerPollIdentifier idPoll, String token)
      throws ResourceNotFoundException, WrongCredentialsException {
    Optional<ServerPoll> pollPo = pollRepository.findById(idPoll);
    Optional<Moderator> poster = moderatorRepository.findById(idModerator);
    Optional<Moderator> test = moderatorRepository.findByToken(token);

    if (pollPo.isEmpty() || poster.isEmpty() || test.isEmpty()) {
      throw new ResourceNotFoundException();
    } else if (!(test.get().equals(poster.get()))
        || !(pollPo.get().getIdPoll().getIdxModerator().equals(poster.get()))) {
      throw new WrongCredentialsException();
    }

    return true;
  }
}
