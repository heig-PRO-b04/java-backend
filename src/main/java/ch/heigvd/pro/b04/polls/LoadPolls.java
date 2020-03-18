package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.moderators.*;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Configuration
public class LoadPolls {

    @Bean
    CommandLineRunner configurePolls(PollRepository repo, ModeratorRepository repoMod) {
        final Moderator test=repoMod.findByIdModerator("david");
        if(test==null)
        {
            throw new RuntimeException("Moderator not found");
        }

        return args -> {
            repo.save(new Poll(test.getIdModerator(), "What do you know about Darth Talon ?"));
        };
    }
}
