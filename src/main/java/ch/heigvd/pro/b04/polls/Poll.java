package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.moderators.Moderator;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@IdClass(PollIdentifier.class)
public class Poll implements Serializable {
    @Id
    @GeneratedValue
    private Long idPoll;
    @Id private String idxModerator;
    //@EmbeddedId private PollIdentifier idPoll;

    /*@Id
    @ManyToOne
    @JoinColumn
    private Moderator idxModerator;*/

    private String title;

    public Poll() {}

    public Poll(String title) {
        //idPoll=new Long(1);
        idxModerator="david";
        this.title = title;

        //pr éviter qu'il crée plein de fois le même pdt le dev
        //idPoll=new PollIdentifier("david");
    }
}
