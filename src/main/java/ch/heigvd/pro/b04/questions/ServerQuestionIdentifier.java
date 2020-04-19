package ch.heigvd.pro.b04.questions;

import ch.heigvd.pro.b04.polls.ServerPoll;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Data
@Embeddable
public class ServerQuestionIdentifier implements Serializable {

  @Column
  @Getter
  private long idServerQuestion;

  @ManyToOne
  @PrimaryKeyJoinColumn
  private ServerPoll idxPoll;

  public ServerQuestionIdentifier() {
  }

  public ServerQuestionIdentifier(long id) {
    this.idServerQuestion = id;
  }

  public ServerPoll getIdxPoll() {
    return idxPoll;
  }
}
