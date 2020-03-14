package ch.heigvd.pro.b04.questions;

import ch.heigvd.pro.b04.polls.PollIdentifier;
import lombok.Data;

import java.io.Serializable;
import java.util.*;
import javax.persistence.EmbeddedId;
import javax.persistence.*;
import javax.persistence.Entity;

@Data
@Embeddable
public class QuestionIdentifier implements Serializable
{
    private PollIdentifier idxPoll;
    @GeneratedValue @Id private Long idQuestion;

    public QuestionIdentifier()
    {}

    public QuestionIdentifier(PollIdentifier idxPoll)
    {
        this.idxPoll=idxPoll;
    }
}
