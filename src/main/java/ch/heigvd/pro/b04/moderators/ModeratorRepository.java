package ch.heigvd.pro.b04.moderators;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ModeratorRepository extends JpaRepository<Moderator, String> {

  Moderator findByIdModerator(String idModerator);
}
