package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.moderators.*;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import java.io.Serializable;
import org.springframework.lang.NonNull;

@Data
@Embeddable
public class PollIdentifier implements Serializable {

    @Column
    private Long idPoll;

    @ManyToOne
    @PrimaryKeyJoinColumn//(name = "moderator")
    private Moderator idxModerator;

    public PollIdentifier() {}

    public PollIdentifier(long id) {
        idPoll=new Long(id);
    }
}
