package ch.heigvd.pro.b04.statistics;

import ch.heigvd.pro.b04.answers.ServerAnswer;
import ch.heigvd.pro.b04.auth.exceptions.WrongCredentialsException;
import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.polls.ServerPoll;
import ch.heigvd.pro.b04.polls.ServerPollIdentifier;
import ch.heigvd.pro.b04.polls.ServerPollRepository;
import ch.heigvd.pro.b04.questions.ServerQuestion;
import ch.heigvd.pro.b04.statistics.ServerPollStatistics.AnswerStatistics;
import ch.heigvd.pro.b04.statistics.ServerPollStatistics.QuestionStatistics;
import ch.heigvd.pro.b04.votes.ServerVote;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

  private final ServerPollRepository polls;
  private final ModeratorRepository moderators;

  /**
   * Creates a new controller for statistics.
   *
   * @param polls      The repository for polls.
   * @param moderators The repository for moderators.
   */
  public StatisticsController(
      ServerPollRepository polls,
      ModeratorRepository moderators
  ) {
    this.polls = polls;
    this.moderators = moderators;
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
    return moderators.findByToken(token)
        .filter(moderator -> moderator.getIdModerator() == idMod);
  }

  /**
   * Returns some basic aggregated statistics for a poll.
   *
   * @param token       The authentication token.
   * @param idModerator The id of the poll moderator.
   * @param idPoll      The id of the poll.
   * @return A new {@link ServerPollStatistics} object.
   * @throws WrongCredentialsException If the credentials do not offer access to the poll.
   * @throws ResourceNotFoundException If the poll could not be found.
   */
  @GetMapping("/mod/{idModerator}/poll/{idPoll}/statistics")
  @Transactional
  public ServerPollStatistics statisticsForPoll(
      @RequestParam(name = "token") String token,
      @PathVariable(name = "idModerator") Integer idModerator,
      @PathVariable(name = "idPoll") Integer idPoll
  ) throws WrongCredentialsException, ResourceNotFoundException {

    Moderator authenticated = findVerifiedModeratorByIdAndToken(idModerator, token)
        .orElseThrow(WrongCredentialsException::new);

    ServerPollIdentifier identifier = ServerPollIdentifier.builder()
        .idxModerator(authenticated)
        .idPoll(idPoll)
        .build();

    ServerPoll poll = polls.findById(identifier)
        .orElseThrow(ResourceNotFoundException::new);

    ServerPollStatistics.ServerPollStatisticsBuilder builder = ServerPollStatistics.builder();

    for (ServerQuestion question : poll.getPollServerQuestions()) {
      QuestionStatistics.QuestionStatisticsBuilder questionBuilder = QuestionStatistics.builder();
      questionBuilder.title(question.getTitle());

      for (ServerAnswer answer : question.getAnswersToQuestion()) {
        AnswerStatistics.AnswerStatisticsBuilder answerBuilder = AnswerStatistics.builder();
        answerBuilder.title(answer.getTitle());

        var groupedByParticipant = answer.getVoteSet()
            .parallelStream()
            // Group the votes by participant.
            .collect(Collectors.groupingBy(serverVote -> serverVote.getIdVote()
                .getIdxParticipant()
                .getIdParticipant()))
            .entrySet()
            .parallelStream()
            // Get the latest vote for the participant.
            .map(participantIdentifierListEntry ->
                participantIdentifierListEntry.getValue().stream()
                    .max(Comparator.comparing(serverVote -> serverVote.getIdVote().getTimeVote()))
                    .map(ServerVote::isAnswerChecked)
                    .orElse(false))
            .collect(Collectors.toUnmodifiableList());

        // Fetch the positive and negative answer counts.
        answerBuilder.negative(((int) groupedByParticipant.stream().filter(b -> !b).count()));
        answerBuilder.positive(((int) groupedByParticipant.stream().filter(b -> b).count()));

        questionBuilder.answer(answerBuilder.build());
      }

      builder.question(questionBuilder.build());
    }

    return builder.build();
  }

}
