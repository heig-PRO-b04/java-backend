package ch.heigvd.pro.b04.answers;

import ch.heigvd.pro.b04.participants.Participant;
import ch.heigvd.pro.b04.votes.Vote;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Answer {

  @EmbeddedId
  private AnswerIdentifier idAnswer;

  private String text;
  private String description;

  @OneToMany(mappedBy = "idVote.idxAnswer", cascade = CascadeType.ALL)
  private Set<Vote> voteSet;

  /**
   * Builds a new {@link Answer} entity.
   *
   * @param id          An identifier for this specific answer.
   * @param title       The title of the answer. Always displayed.
   * @param description The description of the answer. Displayed on demand.
   */
  public Answer(long id, String title, String description) {
    this.idAnswer = new AnswerIdentifier(id);
    this.text = title;
    this.description = description;
  }

  /**
   * add a {@link Vote} to the current {@link Answer}.
   *
   * @param newVote vote to add
   */
  public void addVote(Vote newVote) {
    newVote.getIdVote().setIdxAnswer(this);
    if (voteSet == null) {
      voteSet = Stream.of(newVote).collect(Collectors.toSet());
    } else {
      voteSet.add(newVote);
    }
  }
}
