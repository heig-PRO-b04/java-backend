package ch.heigvd.pro.b04.questions;

import ch.heigvd.pro.b04.answers.Answer;
import ch.heigvd.pro.b04.polls.Poll;
import ch.heigvd.pro.b04.polls.PollIdentifier;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.*;

//@Data
@Entity
public class Question {
    @EmbeddedId private QuestionIdentifier idQuestion;

    @OneToMany(mappedBy = "idAnswer.idxQuestion", cascade = CascadeType.ALL)
    private Set<Answer> answersToQuestion;

    private short indexInPoll;
    private String title, details;
    private boolean visibility;
    private short answersMin, answersMax;

    public Question(){}

    public Question(long id,
                    short index,
                    String title,
                    String details,
                    boolean visible,
                    short min,
                    short max)
    {
        this.idQuestion=new QuestionIdentifier(id);
        this.indexInPoll=index;
        this.title=title;
        this.details=details;
        visibility=visible;
        this.answersMax=max;
        this.answersMin=min;
    }

    public QuestionIdentifier getIdQuestion()
    {
        return idQuestion;
    }

    public void addAnswer(Answer newAnswer)
    {
        newAnswer.getIdAnswer().setIdxQuestion(this);
        if(answersToQuestion==null)
        {
            answersToQuestion= Stream.of(newAnswer).collect(Collectors.toSet());
        }
        else
        {
            answersToQuestion.add(newAnswer);
        }
    }
}
