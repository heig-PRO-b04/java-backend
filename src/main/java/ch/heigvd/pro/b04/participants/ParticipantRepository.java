package ch.heigvd.pro.b04.participants;

import ch.heigvd.pro.b04.sessions.ServerSession;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParticipantRepository extends JpaRepository<Participant, ParticipantIdentifier> {
  Optional<Participant> findByToken(String token);

  @Query("SELECT s FROM ServerSession s "
      + " INNER JOIN Participant p"
      + " ON p.token = :token"
      + " WHERE s = p.idParticipant.idxServerSession")
  Optional<ServerSession> getAssociatedSession(String token);
}