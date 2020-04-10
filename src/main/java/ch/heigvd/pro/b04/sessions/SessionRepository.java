package ch.heigvd.pro.b04.sessions;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, SessionIdentifier> {

}
