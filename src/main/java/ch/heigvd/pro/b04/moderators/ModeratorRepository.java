package ch.heigvd.pro.b04.moderators;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface ModeratorRepository extends JpaRepository<Moderator, String> {

    Moderator findByIdModerator(String id_moderator);
}
