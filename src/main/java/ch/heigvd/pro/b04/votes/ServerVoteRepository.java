package ch.heigvd.pro.b04.votes;

import ch.heigvd.pro.b04.answers.ServerAnswer;
import ch.heigvd.pro.b04.participants.ParticipantIdentifier;
import ch.heigvd.pro.b04.questions.ServerQuestionIdentifier;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServerVoteRepository extends JpaRepository<ServerVote, VoteIdentifier> {

  List<ServerVote> findAllByIdVote_IdxServerAnswer(ServerAnswer answer);

  @Query("SELECT v FROM ServerVote v WHERE"
      + " v.idVote.idxParticipant.idParticipant = :participant AND"
      + " v.idVote.idxServerAnswer.idAnswer.idxServerQuestion.idServerQuestion = :question")
  List<ServerVote> findAllByParticipantAndQuestion(
      ParticipantIdentifier participant,
      ServerQuestionIdentifier question
  );
}
