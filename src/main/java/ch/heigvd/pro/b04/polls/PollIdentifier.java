package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.moderators.*;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import java.io.Serializable;
import org.springframework.lang.NonNull;

@Data
@Embeddable
public class PollIdentifier implements Serializable {

    private String idxModerator;
    @GeneratedValue @NonNull private Long idPoll;

    public PollIdentifier() {}

    public PollIdentifier(String moderatorName) {
        this.idxModerator = moderatorName;
    }

//    public void setModerator(String idxModerator)
//    {
//      this.idxModerator=idxModerator;
    //}
}
