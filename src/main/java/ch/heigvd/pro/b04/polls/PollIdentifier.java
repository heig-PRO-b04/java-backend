package ch.heigvd.pro.b04.polls;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Embeddable
public class PollIdentifier implements Serializable
{
    private Long idxModerator;
    @GeneratedValue @Id private Long idPoll;

    public PollIdentifier()
    {}

    public PollIdentifier(Long moderator)
    {
        this.idxModerator = moderator;
        //this.idPoll = poll;
    }
}
