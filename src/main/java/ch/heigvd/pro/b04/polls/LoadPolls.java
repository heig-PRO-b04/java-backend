package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.moderators.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadPolls {

    @Bean
    CommandLineRunner configurePolls(PollRepository repo) {
        return args -> {
            repo.save(new Poll("david", "Darth Talon"));
        };
    }
}
