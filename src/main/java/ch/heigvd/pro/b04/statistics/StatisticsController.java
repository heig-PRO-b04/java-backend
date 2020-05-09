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
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Set;
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
    List<QuestionStatistics> questionsS = new LinkedList<>();
    for (ServerQuestion question : poll.getPollServerQuestions()) {
      QuestionStatistics.QuestionStatisticsBuilder questionBuilder = QuestionStatistics.builder();
      questionBuilder.title(question.getTitle());
      List<AnswerStatistics> answersS = new LinkedList<>();

      Set<ServerVote> votes = null; //null because Set cannot be instantiated (abstract class)
      //fetch all votes of all answers to a question
      for (ServerAnswer answer : question.getAnswersToQuestion()) {
        if (votes == null) {
          votes = answer.getVoteSet();
        } else {
          votes.addAll(answer.getVoteSet());
        }
      }

      var groupedByParticipant = votes
          .parallelStream()
          // Group the votes by participant.
          .collect(Collectors.groupingBy(serverVote -> serverVote.getIdVote()
              .getIdxParticipant()
              .getIdParticipant()))
          .entrySet()
          .parallelStream()

          // Sort votes by the most recent ones
          .map(participantIdentifierListEntry ->
              participantIdentifierListEntry.getValue().stream()
                  .sorted((a1, a2) -> a1.compareTo(a2))
                  //.limit(question.getAnswersMax())
                  //.max(Comparator.comparing(serverVote -> serverVote.getIdVote().getTimeVote()))
                  //.map(ServerVote::isAnswerChecked)
                  .collect(Collectors.toList()))
          //.orElse(false))
          .collect(Collectors.toUnmodifiableList());
      //at this point, groupedByParticipant is a List<List<ServerVote>>

      short[] positive = new short[question.getAnswersToQuestion().size() * 2];//*2 to avoid surely out of bounds
      short[] negative = new short[question.getAnswersToQuestion().size() * 2];//theoretically (+1) would be enough

      //counting negative and positives votes one participant at a time
      for (short x = 0; x < groupedByParticipant.size(); ++x) {
        short participantCount = 0;
        short y = 0;
        for (; y < groupedByParticipant.get(x).size(); ++y) {
          if (groupedByParticipant.get(x).get(y).isAnswerChecked()) {
            ++participantCount;
            //limit to answersMax positive votes. remember most recent votes are the first ones in the list.
            if (!(participantCount > question.getAnswersMax())) {
              ++positive[(int) groupedByParticipant.get(x).get(y).getIdVote().getIdxServerAnswer()
                  .getIdAnswer()
                  .getIdAnswer()];
            }
          } else {
            ++negative[(int) groupedByParticipant.get(x).get(y).getIdVote().getIdxServerAnswer()
                .getIdAnswer()
                .getIdAnswer()];
          }
        }
        //votes not counted if total lesser than answersMin
        if (participantCount < question.getAnswersMin()) {
          positive[(int) groupedByParticipant.get(x).get(y).getIdVote().getIdxServerAnswer()
              .getIdAnswer()
              .getIdAnswer()] = 0;
        }
      }

      //build statistics
      for (ServerAnswer a : question.getAnswersToQuestion()) {
        AnswerStatistics.AnswerStatisticsBuilder answerBuilder = AnswerStatistics.builder();
        answerBuilder.title(a.getTitle());
        answerBuilder.negative(negative[(int) a.getIdAnswer().getIdAnswer()]);
        answerBuilder.positive(positive[(int) a.getIdAnswer().getIdAnswer()]);
        answersS.add(answerBuilder.build());
      }
      // Fetch the positive and negative answer counts.
      //answerBuilder.negative(((int) groupedByParticipant.stream().filter(b -> b!=b).count()));
      //answerBuilder.positive(((int) groupedByParticipant.stream().filter(b -> b==b).count()));

      //questionBuilder.answer(answers);
      questionBuilder.answers(answersS);
      //}//end foreach answer
      questionsS.add(questionBuilder.build());
      //builder.question(questionBuilder.build());
    }//end foreach question
    builder.questions(questionsS);
    return builder.build();
  }
}
