package ch.heigvd.pro.b04.moderators;

import ch.heigvd.pro.b04.answers.Answer;
import ch.heigvd.pro.b04.polls.Poll;
import ch.heigvd.pro.b04.polls.PollRepository;
import ch.heigvd.pro.b04.questions.Question;
import ch.heigvd.pro.b04.questions.QuestionRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.var;
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
        "And for endless dance night on Coruscant ?", "",
        true, (short)1, (short)1);

    @Bean
    CommandLineRunner configureModerators(ModeratorRepository repository, PollRepository poooools, QuestionRepository qestrepo) {

        david.addPoll(testPoll);
        testPoll.addQuestion(question1);
        testPoll.addQuestion(question2);
        testPoll.addQuestion(question3);

        //yes I'm tired and I drunk whisky
        Answer[] aphra=createAnswers(0);
        for(Answer a:aphra)
        {
            question1.addAnswer(a);
        }
        aphra=createAnswers(4);
        for(Answer a:aphra)
        {
            question2.addAnswer(a);
        }
        aphra=createAnswers(8);
        for(Answer a:aphra)
        {
            question3.addAnswer(a);
        }

        return args -> {
            repository.save(david);
//            repository.save(new Moderator("alexandre", "11"));
//            repository.save(new Moderator("clarisse", "21"));
//            repository.save(new Moderator("david", "33", "Your favorite Togruta or Twi\'lek ?"));
//            repository.save(new Moderator("guy-laurent", "44"));
//            repository.save(new Moderator("matthieu", "55"));
        };
    }

    private Answer[] createAnswers(int id)
    {
        Answer answer1=new Answer(id++, "Darth Talon", "");
        Answer answer2=new Answer(id++, "Shaak Ti", "");
        Answer answer3=new Answer(id++, "Aayla Secura", "");
        Answer answer4=new Answer(id++, "Ahsoka Tano", "");

        Answer[] aphra =new Answer[]{answer1, answer2, answer3, answer4};
        return aphra;
    }
}
