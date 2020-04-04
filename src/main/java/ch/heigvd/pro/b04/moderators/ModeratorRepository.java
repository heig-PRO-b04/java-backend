package ch.heigvd.pro.b04.moderators;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModeratorRepository extends JpaRepository<Moderator, Integer> {

  Optional<Moderator> findByUsername(String username);
}
