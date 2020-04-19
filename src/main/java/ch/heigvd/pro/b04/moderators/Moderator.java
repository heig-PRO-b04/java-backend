package ch.heigvd.pro.b04.moderators;

import ch.heigvd.pro.b04.auth.exceptions.WrongCredentialsException;
import ch.heigvd.pro.b04.polls.ClientPoll;
import ch.heigvd.pro.b04.polls.ServerPoll;
import ch.heigvd.pro.b04.polls.ServerPollIdentifier;
import ch.heigvd.pro.b04.polls.ServerPollRepository;
import ch.heigvd.pro.b04.polls.exceptions.IllegalPollStateException;
import ch.heigvd.pro.b04.polls.exceptions.PollNotExistingException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Exclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode
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

  @Getter
  private String salt;

  @Getter
  private String token;

  @OneToMany(mappedBy = "idPoll.idxModerator", cascade = CascadeType.ALL)
  @Exclude
  private Set<ServerPoll> pollSet;

  /**
   * Returns the {@link ServerPollIdentifier} for this instance of {@link Moderator} and a certain
   * poll disambiguator.
   *
   * @param disambiguator The (moderator-)unique identifier for the poll we're creating.
   * @return The newly created {@link ServerPollIdentifier}.
   */
  public ServerPollIdentifier getPollIdentifier(int disambiguator) {
    return ServerPollIdentifier.builder()
        .idxModerator(this)
        .idPoll(disambiguator)
        .build();
  }

  /**
   * Inserts a new poll in the provided {@link ServerPollRepository} for the current {@link
   * Moderator} instance.
   *
   * @param repository The repository in which the poll is added.
   * @param poll       The poll data.
   * @return The newly inserted poll.
   */
  @Transactional
  public ServerPoll newPoll(ServerPollRepository repository, ClientPoll poll) {

    Long identifier = repository.findAll().stream()
        .map(ServerPoll::getIdPoll)
        .map(ServerPollIdentifier::getIdPoll)
        .max(Long::compareTo)
        .map(id -> id + 1)
        .orElse(1L);

    return repository.save(ServerPoll.builder()
        .idPoll(ServerPollIdentifier.builder()
            .idxModerator(this)
            .idPoll(identifier)
            .build())
        .title(poll.getTitle())
        .build());
  }

  /** Verifies that a given moderator has access to a poll.
   *
   * @param serverPollRepository The serverpoll repository
   * @param idPoll The id of the poll
   * @return The poll associated with idPoll if it belongs to the moderator
   * @throws PollNotExistingException is thrown if the poll doesn't belong to the moderator or
   *         doesn't exist
   */
  public ServerPoll getPollWithId(ServerPollRepository serverPollRepository, Integer idPoll)
      throws PollNotExistingException {

    List<ServerPoll> pollList = serverPollRepository.findByModeratorAndId(this, idPoll);
    if (pollList.isEmpty()) {
      throw new PollNotExistingException();
    } else if (pollList.size() > 1) {
      throw new IllegalPollStateException();
    }

    return pollList.get(0);
  }

  /** Verifies that a given idModerator and token belong to the same moderator.
   *
   * @param moderatorRepository The moderator repository
   * @param idModerator The given moderator id
   * @param token The given token
   * @return Returns a moderator if the token and the id match
   * @throws WrongCredentialsException is thrown when the token and id don't match or if the
   *         moderator doesn't exist.
   */
  public static Moderator verifyModeratorWith(
      ModeratorRepository moderatorRepository,
      Integer idModerator,
      String token) throws WrongCredentialsException {

    Optional<Moderator> modFromId = moderatorRepository.findById(idModerator);
    if (modFromId.isEmpty() || ! modFromId.equals(moderatorRepository.findByToken(token))) {
      throw new WrongCredentialsException();
    }

    return modFromId.get();
  }
}
