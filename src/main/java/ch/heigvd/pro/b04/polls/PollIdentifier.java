package ch.heigvd.pro.b04.polls;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class PollIdentifier implements Serializable
{
    private Long idModerator;
    private Long idPoll;

    public PollIdentifier()
    {}

    public PollIdentifier(Long moderator, Long poll)
    {
        this.idModerator = moderator;
        this.idPoll = poll;
    }
}
