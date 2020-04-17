package ch.heigvd.pro.b04.questions;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, QuestionIdentifier> {

}
