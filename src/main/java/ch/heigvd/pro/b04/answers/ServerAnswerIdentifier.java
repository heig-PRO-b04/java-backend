package ch.heigvd.pro.b04.answers;

import ch.heigvd.pro.b04.questions.ServerQuestion;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ServerAnswerIdentifier implements Serializable {

  @Column
  private long idAnswer;

  @ManyToOne
  @PrimaryKeyJoinColumn
  private ServerQuestion idxServerQuestion;
}