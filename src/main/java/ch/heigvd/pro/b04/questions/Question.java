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

    public Question(){}

    public Question(PollIdentifier idxPoll)
    {
        this.idQuestion=new QuestionIdentifier(idxPoll);
    }
}
