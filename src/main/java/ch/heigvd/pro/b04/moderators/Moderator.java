package ch.heigvd.pro.b04.moderators;

import ch.heigvd.pro.b04.polls.Poll;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.jetbrains.annotations.NotNull;

@Entity
//@Table(name="moderator")
public class Moderator {

    @Id private String idModerator;
    private String secret;

    @OneToMany(mappedBy = "idPoll.idxModerator", cascade = CascadeType.ALL)
    private Set<Poll> pollSet;

    public Moderator() {
    }

    public Moderator(String name, String secret) {

        this.idModerator = name;
        this.secret = secret;
    }

    public Moderator(String name, String secret, String titlePoll) {
        this(name, secret);
        Poll newPoll=new Poll(1,titlePoll);
        newPoll.getIdPoll().setIdxModerator(this);
        this.pollSet= Stream.of(newPoll).collect(Collectors.toSet());
    }

    public String getIdModerator()
    {
        return idModerator;
    }
}
