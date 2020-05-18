package ch.heigvd.pro.b04.sessions;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.participants.ParticipantRepository;
import ch.heigvd.pro.b04.polls.ServerPoll;
import ch.heigvd.pro.b04.polls.ServerPollIdentifier;
import ch.heigvd.pro.b04.polls.ServerPollRepository;
import ch.heigvd.pro.b04.sessions.exceptions.SessionStateMustBeOpenedFirstException;
import ch.heigvd.pro.b04.sessions.exceptions.SessionStateMustBeClosedFirstException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SessionStateTest {
  @InjectMocks
  SessionController sessionController;
  @Mock
  SessionRepository sessionRepository;
  @Mock
  ServerPollRepository serverPollRepository;
  @Mock
  ModeratorRepository moderatorRepository;

  @Test
  public void testNotAllSessionStateTransitionsArePermitted()
  {
    Moderator aloy= Moderator.builder().build();

    ServerSession session= ServerSession.builder().build();

    ServerPoll pollTest= ServerPoll.builder()
        .idPoll(ServerPollIdentifier.builder()
            .idxModerator(aloy)
            .idPoll(1).build())
            .serverSessionSet(Set.of(session))
        .title("The Scorchers").build();

     session= ServerSession.builder()
        .idSession(SessionIdentifier.builder()
        .idSession(1)
        .idxPoll(pollTest).build())
        .state(SessionState.OPEN).build();

    aloy= Moderator.builder()
        .idModerator(1)
        .username("aloy")
        .pollSet(Set.of(pollTest))
        .token("HZD2017").build();

    when(moderatorRepository.findByToken(aloy.getToken())).thenReturn(Optional.of(aloy));
    when(moderatorRepository.findById(1)).thenReturn(Optional.of(aloy));
    when(sessionRepository.findByModAndPoll(Mockito.any(), Mockito.any())).thenReturn(List.of(session));
    lenient().when(moderatorRepository.saveAndFlush(Mockito.any())).thenReturn(AdditionalAnswers.returnsFirstArg());
    lenient().when(serverPollRepository.findByModeratorAndId(aloy, 1)).thenReturn(Optional.of(pollTest));

    assertDoesNotThrow(()->sessionController.putSession(1,1,"HZD2017",
        ClientSession.builder()
            .state(SessionState.CLOSED).build()));
    assertDoesNotThrow(()->sessionController.putSession(1,1,"HZD2017",
        ClientSession.builder()
            .state(SessionState.OPEN).build()));
    session.setState(SessionState.OPEN);
    assertDoesNotThrow(()->sessionController.putSession(1,1,"HZD2017",
        ClientSession.builder()
            .state(SessionState.QUARANTINED).build()));
    assertDoesNotThrow(()->sessionController.putSession(1,1,"HZD2017",
        ClientSession.builder()
            .state(SessionState.CLOSED).build()));
    assertThrows(SessionStateMustBeOpenedFirstException.class, ()->sessionController.putSession(1,1,"HZD2017",
        ClientSession.builder()
            .state(SessionState.QUARANTINED).build()));
    session.setState(SessionState.QUARANTINED);
    assertThrows(SessionStateMustBeClosedFirstException.class, ()->sessionController.putSession(1,1,"HZD2017",
        ClientSession.builder()
            .state(SessionState.OPEN).build()));
  }
}
