package ch.heigvd.pro.b04.questions;

import ch.heigvd.pro.b04.polls.Poll;
import ch.heigvd.pro.b04.polls.PollIdentifier;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Embeddable
public class QuestionIdentifier implements Serializable {
    @Column
    private long idQuestion;

    @ManyToOne @JoinColumn
    private Poll idxPoll;

    public QuestionIdentifier(){}

    public QuestionIdentifier(long id)
    {
        this.idQuestion=id;
    }
}
