package ch.heigvd.pro.b04.answers;

import ch.heigvd.pro.b04.questions.ServerQuestion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<ServerAnswer, ServerAnswerIdentifier> {

  List<ServerAnswer> findAllByIdAnswerIdxServerQuestion(ServerQuestion question);
}