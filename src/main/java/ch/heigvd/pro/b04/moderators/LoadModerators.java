package ch.heigvd.pro.b04.moderators;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadModerators {

    @Bean
    CommandLineRunner configureModerators(ModeratorRepository repository) {
        return args -> {
            repository.save(new Moderator("alexandre", "11"));
            repository.save(new Moderator("clarisse", "21"));
            repository.save(new Moderator("david", "33"));
            repository.save(new Moderator("guy-laurent", "44"));
            repository.save(new Moderator("matthieu", "55"));

        };
    }
}
