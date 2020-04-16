package ch.heigvd.pro.b04.participants;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, ParticipantIdentifier> {
  Optional<Participant> findByToken(String token);
}