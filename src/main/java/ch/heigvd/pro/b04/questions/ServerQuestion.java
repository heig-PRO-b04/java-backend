package ch.heigvd.pro.b04.questions;

import ch.heigvd.pro.b04.answers.AnswerRepository;
import ch.heigvd.pro.b04.answers.ClientAnswer;
import ch.heigvd.pro.b04.answers.ServerAnswer;
import ch.heigvd.pro.b04.answers.ServerAnswerIdentifier;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor
public class ServerQuestion {

  @Setter
  @Getter
  protected double indexInPoll;
  @Setter
  @Getter
  protected String title;
  @Setter
  @Getter
  protected String details;
  @Setter
  @Getter
  protected QuestionVisibility visibility;
  @Setter
  @Getter
  protected short answersMin;
  @Setter
  @Getter
  protected short answersMax;
  @Getter
  @EmbeddedId
  private ServerQuestionIdentifier idServerQuestion;
  @OneToMany(mappedBy = "idAnswer.idxServerQuestion", cascade = CascadeType.ALL)
  private Set<ServerAnswer> answersToQuestion;

  /**
   * Insert a new {@link ServerAnswer} to this {@link ServerQuestion}.
   *
   * @param repoA answer repository
   * @param newAnswer {@link ClientAnswer} to insert as a {@link ServerAnswer}
   * @return {@link ServerAnswer} inserted
   */
  public ServerAnswer addAnswer(AnswerRepository repoA, ClientAnswer newAnswer) {
    ServerAnswer sombra = ServerAnswer.builder()
        .idAnswer(ServerAnswerIdentifier.builder()
            .idAnswer(getNewIdentifier(repoA))
            .idxServerQuestion(this).build())
        .title(newAnswer.getTitle())
        .description(newAnswer.getDescription()).build();

    return repoA.save(sombra);
  }

  /**
   * Creates a new unique identifier for a {@link ServerQuestion}.
   *
   * @param repository The repository to which we will add a new Session to
   * @return A new unique identifier
   */
  public static Long getNewIdentifier(AnswerRepository repository) {
    return repository.findAll().stream()
        .map(ServerAnswer::getIdAnswer)
        .map(ServerAnswerIdentifier::getIdAnswer)
        .max(Long::compareTo)
        .map(id -> id + 1)
        .orElse(1L);
  }
}
