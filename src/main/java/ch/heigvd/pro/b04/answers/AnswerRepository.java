package ch.heigvd.pro.b04.answers;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<ServerAnswer, ServerAnswerIdentifier> {}