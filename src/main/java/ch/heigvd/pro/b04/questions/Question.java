package ch.heigvd.pro.b04.questions;

import ch.heigvd.pro.b04.polls.PollIdentifier;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.*;

@Data
@Entity
public class Question {
    @EmbeddedId private QuestionIdentifier idQuestion;
    private short indexInPoll;
    private String title, details;
    private boolean visibility;
    private short answersMin, answersMax;

    public Question(){}

    public Question(PollIdentifier idxPoll,
                    short index,
                    String title,
                    String details,
                    boolean visible,
                    short min,
                    short max)
    {
        this.idQuestion=new QuestionIdentifier(idxPoll);
        this.indexInPoll=index;
        this.title=title;
        this.details=details;
        visibility=visible;
        this.answersMax=max;
        this.answersMin=min;
    }
}
