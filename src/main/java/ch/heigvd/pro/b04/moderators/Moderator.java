package ch.heigvd.pro.b04.moderators;

import ch.heigvd.pro.b04.polls.Poll;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class Moderator {

  @Id
  private String idModerator;
  private String secret;

  @OneToMany(mappedBy = "idPoll.idxModerator", cascade = CascadeType.ALL)
  private Set<Poll> pollSet;

  public Moderator() {
  }

  public Moderator(String name, String secret) {
    this.idModerator = name;
    this.secret = secret;
  }

  public void addPoll(Poll newPoll) {
    newPoll.getIdPoll().setIdxModerator(this);
    this.pollSet = Stream.of(newPoll).collect(Collectors.toSet());
  }

  public String getIdModerator() {
    return idModerator;
  }

  public String getUsername() {
    return idModerator;
  }

  public String getSecret() {
    return secret;
  }
}
