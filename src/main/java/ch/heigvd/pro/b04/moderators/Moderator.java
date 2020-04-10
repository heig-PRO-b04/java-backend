package ch.heigvd.pro.b04.moderators;

import ch.heigvd.pro.b04.polls.Poll;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class Moderator {

  @Id
  @Getter
  @GeneratedValue
  private int idModerator;

  @Getter
  private String username;

  @Getter
  private String secret;

  @OneToMany(mappedBy = "idPoll.idxModerator", cascade = CascadeType.ALL)
  private Set<Poll> pollSet;

  /**
   * add a new Poll {@link Poll} to the polls of this moderator.
   *
   * @param newPoll poll to add
   */
  public void addPoll(Poll newPoll) {
    newPoll.getIdPoll().setIdxModerator(this);
    if (pollSet == null) {
      this.pollSet = Stream.of(newPoll).collect(Collectors.toSet());
    } else {
      pollSet.add(newPoll);
    }
  }
}
