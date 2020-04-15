package ch.heigvd.pro.b04.participants;

import ch.heigvd.pro.b04.votes.Vote;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Participant {

  @Getter
  @EmbeddedId
  private ParticipantIdentifier idParticipant;

  private String username;

  @OneToMany(mappedBy = "idVote.idxParticipant", cascade = CascadeType.ALL)
  private Set<Vote> voteSet;

  public Participant(long id, String username) {
    idParticipant = new ParticipantIdentifier(id);
    this.username = username;
  }

  /**
   * add a {@link Vote} to the current {@link Participant}.
   *
   * @param newVote vote to add
   */
  public void addVote(Vote newVote) {
    newVote.getIdVote().setIdxParticipant(this);
    if (voteSet == null) {
      voteSet = Stream.of(newVote).collect(Collectors.toSet());
    } else {
      voteSet.add(newVote);
    }
  }
}