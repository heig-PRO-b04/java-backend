package ch.heigvd.pro.b04.questions;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

public class LoadQuestions {

  /**
   * Stores a question into the database.
   *
   * @param repo Repository of the DB, let Spring magic assign this parameter
   * @return thing for Spring magic
   */
  @Bean
  CommandLineRunner configureQuestions(QuestionRepository repo) {
    return args -> {
      //repo.save(new Question);
    };
  }
}
