package ch.heigvd.pro.b04.moderators;

import ch.heigvd.pro.b04.polls.Poll;
import ch.heigvd.pro.b04.polls.PollRepository;
import ch.heigvd.pro.b04.questions.Question;
import ch.heigvd.pro.b04.questions.QuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadModerators {
    Moderator david=(new Moderator("david", "33"));//, "Your favorite Togruta or Twi\'lek ?"));
    Poll testPoll=new Poll(1,"Your favorite Togruta or Twi\'lek ?");

    Question question1=new Question(1, (short)1,
        "Who would you rather spend a candlelit dinner with on Canto Byte ?", "",
        true, (short)1, (short)1);
    Question question2=new Question(2, (short)2,
        "And who would you choose for a torrid night on the beaches of Scarif ?", "",
        true, (short)1, (short)1);
    Question question3=new Question(3, (short)3,
        "And for endless dancing night on Coruscant ?", "",
        true, (short)1, (short)1);

    @Bean
    CommandLineRunner configureModerators(ModeratorRepository repository, PollRepository poooools, QuestionRepository qestrepo) {

        david.addPoll(testPoll);
        testPoll.addQuestion(question1);
        testPoll.addQuestion(question2);
        testPoll.addQuestion(question3);
        return args -> {
            repository.save(david);
            //testPoll.addQuestion(cantoByte);
            //david.addPoll(testPoll);
            //repository.save(david);
            //poooools.save(testPoll);
            //qestrepo.save(cantoByte);
//            repository.save(new Moderator("alexandre", "11"));
//            repository.save(new Moderator("clarisse", "21"));
//            repository.save(new Moderator("david", "33", "Your favorite Togruta or Twi\'lek ?"));
//            repository.save(new Moderator("guy-laurent", "44"));
//            repository.save(new Moderator("matthieu", "55"));
        };
    }
}
