package ch.heigvd.pro.b04.moderators;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModeratorRepository extends JpaRepository<Moderator, Integer> {

  Optional<Moderator> findByUsername(String username);

  /**
   * Returns the {@link Moderator} instance with the provided secret, if it exists. With this
   * information, it will be possible to check if the moderator id is known.
   *
   * @param token The secret that is being searched for.
   * @return An optional with the {@link Moderator} that was found.
   */
  Optional<Moderator> findBySecret(String token);
}
