package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.moderators.*;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import java.io.Serializable;
import org.springframework.lang.NonNull;

//@Data
//@Embeddable
public class PollIdentifier implements Serializable {

    //@Column
    @GeneratedValue
    private Long idPoll;

    //@Column
    private String idxModerator;

    public PollIdentifier() {}

    public PollIdentifier(String moderatorName) {
        this.idxModerator = "david";
        //idPoll=new Long(1);
    }

//    public void setModerator(String idxModerator)
//    {
//      this.idxModerator=idxModerator;
    //}
}
