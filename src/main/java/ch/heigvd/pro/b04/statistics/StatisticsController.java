package ch.heigvd.pro.b04.statistics;

import ch.heigvd.pro.b04.answers.AnswerRepository;
import ch.heigvd.pro.b04.answers.ServerAnswer;
import ch.heigvd.pro.b04.auth.exceptions.WrongCredentialsException;
import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.polls.ServerPoll;
import ch.heigvd.pro.b04.polls.ServerPollIdentifier;
import ch.heigvd.pro.b04.polls.ServerPollRepository;
import ch.heigvd.pro.b04.questions.QuestionRepository;
import ch.heigvd.pro.b04.questions.ServerQuestion;
import ch.heigvd.pro.b04.questions.ServerQuestionIdentifier;
import ch.heigvd.pro.b04.statistics.ServerPollStatistics.AnswerStatistics;
import ch.heigvd.pro.b04.statistics.ServerPollStatistics.QuestionStatistics;
import ch.heigvd.pro.b04.statistics.ServerQuestionStatistics.Answer;
import ch.heigvd.pro.b04.statistics.ServerQuestionStatistics.Timestamp;
import ch.heigvd.pro.b04.statistics.ServerQuestionStatistics.VoteCount;
import ch.heigvd.pro.b04.votes.ServerVote;
import ch.heigvd.pro.b04.votes.ServerVoteRepository;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

  private final AnswerRepository answers;
  private final ServerPollRepository polls;
  private final ModeratorRepository moderators;
  private final QuestionRepository questions;
  private final ServerVoteRepository votes;

  /**
   * Creates a new controller for statistics.
   *
   * @param answers    The repository for answers.
   * @param polls      The repository for polls.
   * @param moderators The repository for moderators.
   * @param questions  The repository for questions.
   */
  public StatisticsController(
      AnswerRepository answers,
      ServerPollRepository polls,
      ModeratorRepository moderators,
      QuestionRepository questions,
      ServerVoteRepository votes
  ) {
    this.answers = answers;
    this.polls = polls;
    this.moderators = moderators;
    this.questions = questions;
    this.votes = votes;
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
   * Returns the statistics for a certain question, at a certain timestamp.
   *
   * @param token       The authentication token.
   * @param idModerator The identifier of the moderator.
   * @param idPoll      The identifier of the poll.
   * @param idQuestion  The identifier of the question.
   * @param timestamps  The timestamps for the query, in seconds.
   * @return The statistics for the question.
   * @throws ResourceNotFoundException If the question does not exist.
   * @throws WrongCredentialsException If the credentials are not authorized.
   */
  @PostMapping("/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/statistics")
  @Transactional
  public ServerQuestionStatistics statisticsForQuestion(
      @RequestParam(name = "token") String token,
      @PathVariable(name = "idModerator") int idModerator,
      @PathVariable(name = "idPoll") int idPoll,
      @PathVariable(name = "idQuestion") int idQuestion,
      @RequestBody List<Integer> timestamps
  ) throws ResourceNotFoundException, WrongCredentialsException {

    Moderator authenticated = findVerifiedModeratorByIdAndToken(idModerator, token)
        .orElseThrow(WrongCredentialsException::new);

    // 0. Start building the return value.
    ServerQuestionStatistics.ServerQuestionStatisticsBuilder builder =
        ServerQuestionStatistics.builder();

    // 1. Fetch all the available answers.
    ServerPoll poll = polls.findById(ServerPollIdentifier.builder()
        .idPoll(idPoll)
        .idxModerator(authenticated)
        .build())
        .orElseThrow(ResourceNotFoundException::new);

    ServerQuestion question = questions.findById(ServerQuestionIdentifier.builder()
        .idServerQuestion(idQuestion)
        .idxPoll(poll)
        .build())
        .orElseThrow(ResourceNotFoundException::new);

    // 1.a Add all the fetched answers.
    List<ServerAnswer> choices = answers.findAllByIdAnswerIdxServerQuestion(question);

    choices.forEach(serverAnswer ->
        builder.answer(Answer.builder()
            .id((int) serverAnswer.getIdAnswer().getIdAnswer())
            .title(serverAnswer.getTitle())
            .build())
    );

    // 2. For all timestamps, count the votes at that time.
    for (int timestamp : timestamps) {

      Timestamp.TimestampBuilder timestampBuilder = Timestamp.builder()
          .seconds(timestamp);

      choices.forEach(serverAnswer -> {
        long count =
            votes.findAllByIdVote_IdxServerAnswer(serverAnswer)
                .stream()
                // Votes that are anterior to the provided timestamp.
                .filter(serverVote ->
                    (serverVote.getIdVote().getTimeVote().toInstant().toEpochMilli() / 1000)
                        < timestamp)
                // Group votes by participant identifier.
                .collect(Collectors.groupingBy(vote ->
                    vote.getIdVote().getIdxParticipant()
                        .getIdParticipant()))
                .values()
                .stream()
                // For each participant, we're only interested in the last vote.
                .map(serverVotes -> serverVotes
                    .stream().max(
                        Comparator.comparing(serverVote -> serverVote.getIdVote().getTimeVote()))
                    .map(ServerVote::isAnswerChecked)
                    .orElse(false))
                // Only filter positive votes.
                .filter(b -> b)
                .count();

        // Add this data to the builder.
        VoteCount voteCount = VoteCount.builder()
            .count((int) count)
            .idAnswer((int) serverAnswer.getIdAnswer().getIdAnswer())
            .build();

        timestampBuilder.vote(voteCount);
      });

      // Add the timestamp.
      builder.timestamp(timestampBuilder.build());
    }

    // Finally.
    return builder.build();
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
      questionBuilder.index(question.getIndexInPoll());
      questionBuilder.title(question.getTitle());
      List<AnswerStatistics> answersS = new LinkedList<>();

      Set<ServerVote> votes = Set.of(); //null because Set cannot be instantiated (abstract class)
      //fetch all votes of all answers to a question
      for (ServerAnswer answer : question.getAnswersToQuestion()) {
        if (votes.isEmpty()) {
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
                  .collect(Collectors.toList()))
          .collect(Collectors.toUnmodifiableList());
      //at this point, groupedByParticipant is a List<List<ServerVote>>

      Map<Long, Short> posV = new HashMap<>();
      Map<Long, Short> negV = new HashMap<>();
      //counting negative and positives votes one participant at a time
      for (List<ServerVote> serverVotes : groupedByParticipant) {
        short participantCount = 0;
        short y = 0;
        for (; y < serverVotes.size(); ++y) {
          if (serverVotes.get(y).isAnswerChecked()) {
            ++participantCount;
            //limit to answersMax positive votes.
            // remember most recent votes are the first ones in the list.
            if (!(participantCount > question.getAnswersMax())) {
              posV.merge(
                  serverVotes.get(y).getIdVote().getIdxServerAnswer().getIdAnswer().getIdAnswer(),
                  (short) 1, (old, newV) -> ++old);
            }
          } else {
            negV.merge(
                serverVotes.get(y).getIdVote().getIdxServerAnswer().getIdAnswer().getIdAnswer(),
                (short) 1, (old, newV) -> ++old);
          }
        }
        --y; // to compensate the ++y before quitting the loop
        //votes not counted if total lesser than answersMin
        if (participantCount < question.getAnswersMin()) {
          posV.put(serverVotes.get(y).getIdVote().getIdxServerAnswer().getIdAnswer().getIdAnswer(),
              (short) 0);
        }
      }

      //build statistics
      for (ServerAnswer a : question.getAnswersToQuestion()) {
        AnswerStatistics.AnswerStatisticsBuilder answerBuilder = AnswerStatistics.builder();
        long key = a.getIdAnswer().getIdAnswer();
        answerBuilder.idAnswer(a.getIdAnswer().getIdAnswer());
        answerBuilder.title(a.getTitle());
        answerBuilder.negative(negV.containsKey(key) ? negV.get(a.getIdAnswer().getIdAnswer()) : 0);
        answerBuilder.positive(posV.containsKey(key) ? posV.get(a.getIdAnswer().getIdAnswer()) : 0);
        answersS.add(answerBuilder.build());
      }

      questionBuilder.answers(answersS);
      //}//end foreach answer
      questionsS.add(questionBuilder.build());
    } //end foreach question
    builder.questions(questionsS);
    return builder.build();
  }
}
