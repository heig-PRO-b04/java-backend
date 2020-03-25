package ch.heigvd.pro.b04.moderators;

import ch.heigvd.pro.b04.answers.Answer;

import ch.heigvd.pro.b04.polls.Poll;
import ch.heigvd.pro.b04.questions.Question;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This method creates a complete poll to test insert and recuperation requests
 */
@Configuration
public class LoadModerators {

  //creation of a moderator
  Moderator david = (new Moderator("david", "33"));

  //creation of a poll
  Poll testPoll = new Poll(1, "Your favorites Togrutas and Twi\'leks ?");

  //creation of 3 questions
  Question question1 = new Question(1, (short) 1,
      "Who would you rather spend a candlelit dinner with on Canto Byte ?", "",
      true, (short) 1, (short) 1);
  Question question2 = new Question(2, (short) 2,
      "And who would you choose for a torrid night on the beaches of Scarif ?", "",
      true, (short) 1, (short) 1);
  Question question3 = new Question(3, (short) 3,
      "And for endless dance night on Coruscant ?", "",
      true, (short) 1, (short) 1);

  // creation of answers
  // all 3 questions have same answers
  {
    for (Answer a : createAnswers(0)) {
      question1.addAnswer(a);
    }
    for (Answer a : createAnswers(4)) {
      question2.addAnswer(a);
    }
    for (Answer a : createAnswers(8)) {
      question3.addAnswer(a);
    }
  }

  @Bean
  CommandLineRunner configureModerators(ModeratorRepository repository) {
    //ASSIGNATION NEEDS TO BE DONE IN THIS ORDER
    //assignation of a poll to his moderator
    david.addPoll(testPoll);
    //assignation of questions to their poll
    testPoll.addQuestion(question1);
    testPoll.addQuestion(question2);
    testPoll.addQuestion(question3);

    //by the Spring magic, this line stores not only the moderator,
    //but also his poll with his questions with his answers
    return args -> {
      repository.save(david);
    };
  }

  /**
   * Auxiliary method to create hard-coded answers Will probably be deleted once in production
   *
   * @param id id to assign to the first of the 4 answers
   * @return array of answers
   */
  private Answer[] createAnswers(int id) {
    Answer answer1 = new Answer(id++, "Darth Talon", "");
    Answer answer2 = new Answer(id++, "Shaak Ti", "");
    Answer answer3 = new Answer(id++, "Aayla Secura", "");
    Answer answer4 = new Answer(id++, "Ahsoka Tano", "");

    Answer[] aphra = new Answer[]{answer1, answer2, answer3, answer4};
    return aphra;
  }
}
