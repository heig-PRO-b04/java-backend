package ch.heigvd.pro.b04.participants;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, ParticipantIdentifier> {

}
