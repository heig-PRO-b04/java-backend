package ch.heigvd.pro.b04.answers;

import ch.heigvd.pro.b04.votes.Vote;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServerAnswer {

  @EmbeddedId
  private ServerAnswerIdentifier idAnswer;

  private String text;
  private String description;

  @OneToMany(mappedBy = "idVote.idxServerAnswer", cascade = CascadeType.ALL)
  private Set<Vote> voteSet;
}
