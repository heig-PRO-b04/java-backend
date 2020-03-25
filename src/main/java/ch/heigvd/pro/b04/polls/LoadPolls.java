package ch.heigvd.pro.b04.polls;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadPolls {

  /**
   * Stores a poll into the database
   * Ununsed for now
   * @param repo repository of the DB, let Spring magic assign this parameter
   * @return thing for Spring magic
   */
  @Bean
    CommandLineRunner configurePolls(PollRepository repo ) {

        return args -> {
            //repo.save(new Poll)
        };
    }
}
