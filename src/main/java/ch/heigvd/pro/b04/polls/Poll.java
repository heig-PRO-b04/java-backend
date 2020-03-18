package ch.heigvd.pro.b04.polls;

import ch.heigvd.pro.b04.moderators.Moderator;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Data
@Entity
public class Poll {
    @EmbeddedId private PollIdentifier idPoll;
    private String title;

    public Poll() {
    }

    public Poll(String idxModerator, String title) {
        this.title = title;
        this.idPoll=new PollIdentifier();
        this.idPoll.setIdxModerator(idxModerator);
    }
}
