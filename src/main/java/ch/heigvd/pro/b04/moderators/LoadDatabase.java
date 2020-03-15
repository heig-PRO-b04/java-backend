package ch.heigvd.pro.b04.moderators;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

  @Bean
  CommandLineRunner configure(ModeratorRepository repository) {
    return args -> {
      repository.save(new Moderator("alexandre"));//, "1"));
      repository.save(new Moderator("clarisse"));//, "2"));
      repository.save(new Moderator("david"));//, "3"));
      repository.save(new Moderator("guy-laurent"));//, "4"));
      repository.save(new Moderator("matthieu"));//, "5"));
    };
  }
}
