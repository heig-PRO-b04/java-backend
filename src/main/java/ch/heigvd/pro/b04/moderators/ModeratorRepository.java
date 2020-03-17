package ch.heigvd.pro.b04.moderators;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ModeratorRepository extends JpaRepository<Moderator, String> {}
