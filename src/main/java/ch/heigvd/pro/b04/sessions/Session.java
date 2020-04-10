package ch.heigvd.pro.b04.sessions;

import ch.heigvd.pro.b04.utils.Constants;
import ch.heigvd.pro.b04.utils.Constants.SessionState;
import java.sql.Timestamp;
import java.util.Random;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
public class Session {

  @EmbeddedId
  private SessionIdentifier idSession;

  @Getter
  @Setter
  private Timestamp start;
  @Getter
  @Setter
  private Timestamp end;
  @Getter
  private int code;
  @Getter
  @Setter
  private Constants.SessionState state;

  public Session(long id) {
    idSession = new SessionIdentifier(id);
    state = SessionState.OPEN; //TODO: set to closed by default
    code= new Random().nextInt(0xFFFf);
  }
}
