package ch.heigvd.pro.b04.sessions;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, SessionIdentifier> {
  Optional<Session> findByCode(String code);
}