package ch.heigvd.pro.b04.questions;

import ch.heigvd.pro.b04.auth.UserCredentials;
import ch.heigvd.pro.b04.auth.exceptions.WrongCredentialsException;
import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.participants.Participant;
import ch.heigvd.pro.b04.participants.ParticipantRepository;
import ch.heigvd.pro.b04.polls.ServerPoll;
import ch.heigvd.pro.b04.polls.ServerPollIdentifier;
import ch.heigvd.pro.b04.polls.ServerPollRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private final QuestionRepository repository;
  @Autowired
  private ServerPollRepository pollRepository;
  @Autowired
  private ParticipantRepository participantRepository;
  @Autowired
  private ModeratorRepository moderatorRepository;

  public QuestionController(QuestionRepository repo) {
    repository = repo;
  }

  @RequestMapping(value = "/mod/{idModerator}/poll/{idPoll}/question", method = RequestMethod.GET)
  List<Question> all(@PathVariable(name = "idPoll") ServerPollIdentifier idPoll,
      @RequestParam(name = "token") String token) throws ResourceNotFoundException {

    Optional<Participant> pollT = participantRepository.findByToken(token);
    if (pollT.isEmpty()) {
      throw new ResourceNotFoundException();
    }
    ServerPoll pA = pollT.get().getIdParticipant().getIdxSession().getIdSession().getIdxPoll();

    Optional<ServerPoll> pollConcerned = pollRepository.findById(idPoll);
    if (pollConcerned.isEmpty()) {
      throw new ResourceNotFoundException();
    } else if (!(pA.equals(pollConcerned.get()))) {
      throw new WrongCredentialsException();
    }

    return (List<Question>) pollConcerned.get().getPollQuestions();
  }

  @RequestMapping(value = "/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}", method = RequestMethod.GET)
  Question byId(@PathVariable(name = "idQuestion") QuestionIdentifier idQ,
      @PathVariable(name = "idPoll") ServerPollIdentifier idPoll) {
    return null;
  }

  @PostMapping(value = "/mod/{idModerator}/poll/{idPoll}/question")
  public Question insertQuestion(@RequestParam(name = "token") String token,
      @RequestBody Question question,
      @PathVariable(name = "idModerator") int idModerator,
      @PathVariable(name = "idPoll") ServerPollIdentifier idPoll) throws ResourceNotFoundException {

    testModeratorRight(idModerator, idPoll, token);

    return pollRepository.findById(idPoll).get().newQuestion(repository, question);
  }

  @PutMapping(value = "/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}")
  public void updateQuestion(@RequestParam(name = "token") String token,
      @RequestBody Question question,
      @PathVariable(name = "idModerator") int idModo,
      @PathVariable(name = "idPoll") ServerPollIdentifier idPoll,
      @PathVariable(name = "idQuestion") QuestionIdentifier maggieQ)
      throws ResourceNotFoundException {
    testModeratorRight(idModo, idPoll, token);
    ServerPoll pollTest=pollRepository.findById(idPoll).get();
    Optional<Question> upQ = repository.findById(maggieQ);
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

  private boolean testModeratorRight(int idModerator, ServerPollIdentifier idPoll, String token)
      throws ResourceNotFoundException {
    Optional<ServerPoll> pollPo = pollRepository.findById(idPoll);
    Optional<Moderator> poster = moderatorRepository.findById(idModerator);
    Optional<Moderator> test = moderatorRepository.findByToken(token);

    if (pollPo.isEmpty() || poster.isEmpty() || test.isEmpty()) {
      throw new ResourceNotFoundException();
    } else if (!(test.get().equals(poster.get())) ||
        !(pollPo.get().getIdPoll().getIdxModerator().equals(poster.get()))) {
      throw new WrongCredentialsException();
    }

    return true;
  }
}
