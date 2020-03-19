package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.questions.Question;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Poll implements Serializable {

    @EmbeddedId private PollIdentifier idPoll;

    @OneToMany(mappedBy = "idQuestion.idxPoll", cascade = CascadeType.ALL)
    private Set<Question> pollQuestions;

    private String title;

    public Poll() {}

    public Poll(long id, String title) {
        idPoll=new PollIdentifier(id);
        this.title = title;
    }

    public PollIdentifier getIdPoll()
    {
        return idPoll;
    }

    public void addQuestion(Question newQuestion)
    {
        newQuestion.getIdQuestion().setIdxPoll(this);
        pollQuestions= Stream.of(newQuestion).collect(Collectors.toSet());
        System.out.println("Wouf wouf");
    }
}
