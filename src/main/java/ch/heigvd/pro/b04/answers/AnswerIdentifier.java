package ch.heigvd.pro.b04.answers;

import ch.heigvd.pro.b04.questions.Question;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import lombok.Data;

@Data
@Embeddable
public class AnswerIdentifier implements Serializable {

    @Column private long idAnswer;

    @ManyToOne
    @PrimaryKeyJoinColumn
    private Question idxQuestion;

    public AnswerIdentifier() {}

    public AnswerIdentifier(long id)
    {
        this.idAnswer=id;
    }
}