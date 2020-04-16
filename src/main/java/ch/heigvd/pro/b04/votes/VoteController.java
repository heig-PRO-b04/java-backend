package ch.heigvd.pro.b04.votes;

import ch.heigvd.pro.b04.answers.Answer;
import ch.heigvd.pro.b04.answers.AnswerIdentifier;
import ch.heigvd.pro.b04.answers.AnswerRepository;
import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;
import ch.heigvd.pro.b04.participants.Participant;
import ch.heigvd.pro.b04.participants.ParticipantRepository;
import ch.heigvd.pro.b04.sessions.Session;
import ch.heigvd.pro.b04.sessions.Session.State;
import ch.heigvd.pro.b04.sessions.SessionRepository;
import ch.heigvd.pro.b04.sessions.exceptions.SessionNotAvailableException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
public class VoteController {

  private final ParticipantRepository participantRepository;
  private final VoteRepository voteRepository;
  private final AnswerRepository answerRepository;
  private final SessionRepository sessionRepository;

  /**
   * Create and store a new {@link Vote}.
   *
   * @param token voter's token
   * @param chekced if this answer is choosed or not
   * @param idAnswer answer concerned by vote
   * @param sessionCode code of the session
   * @throws ResourceNotFoundException if Participant or Answer doesn't exist
   * @throws SessionNotAvailableException if Session is in state Closed
   */
  @PutMapping(value = "/mod/{idModerator}/poll/{idPoll}/session/{sessionCode}"
      + "/question/{idQuestion}/answer/{idAnswer}/vote")
  public void newVote(@RequestParam(name = "token") String token,
      @RequestBody boolean chekced,
      @PathVariable(name = "idAnswer") AnswerIdentifier idAnswer,
      @PathVariable(name = "sessionCode") String sessionCode)
      throws ResourceNotFoundException, SessionNotAvailableException {
    Optional<Participant> voter = participantRepository.findByToken(token);
    Optional<Answer> answerChanged = answerRepository.findById(idAnswer);
    Optional<Session> session = sessionRepository.findByCode(sessionCode);

    if (voter.isEmpty() || answerChanged.isEmpty() || session.isEmpty()) {
      throw new ResourceNotFoundException();
    } else if (session.get().getState() == State.CLOSED) {
      throw new SessionNotAvailableException();
    }

    Vote newVote = Vote.builder()
        .idVote(VoteIdentifier.builder()
            .idxParticipant(voter.get())
            .idxAnswer(answerChanged.get())
            .build())
        .answerChecked(chekced)
        .build();

    voteRepository.saveAndFlush(newVote);
  }
}
