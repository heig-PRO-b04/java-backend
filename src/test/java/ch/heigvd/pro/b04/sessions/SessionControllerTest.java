package ch.heigvd.pro.b04.sessions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import ch.heigvd.pro.b04.auth.exceptions.WrongCredentialsException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.polls.ServerPoll;
import ch.heigvd.pro.b04.polls.ServerPollIdentifier;
import ch.heigvd.pro.b04.polls.ServerPollRepository;
import ch.heigvd.pro.b04.polls.exceptions.PollNotExistingException;
import ch.heigvd.pro.b04.sessions.exceptions.SessionCodeNotHexadecimalException;
import ch.heigvd.pro.b04.sessions.exceptions.SessionNotAvailableException;
import ch.heigvd.pro.b04.sessions.exceptions.SessionNotExistingException;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {

    @InjectMocks
    SessionController sessionController;

    @Mock
    SessionRepository sessionRepository;

    @Mock
    ServerPollRepository serverPollRepository;

    @Mock
    ModeratorRepository moderatorRepository;

    @Test
    public void testIfSessionIsClosed() {
        String code = "0x123F";
        ServerSession currentServerSession = ServerSession.builder()
            .code(code)
            .state(SessionState.CLOSED)
            .build();

        SessionCode sessionCode = SessionCode.builder().hexadecimal(code).build();
        when(sessionRepository.findByCode(code)).thenReturn(Optional.of(currentServerSession));

        assertThrows(SessionNotAvailableException.class,
            () -> sessionController.byCode(sessionCode));
    }

    @Test
    public void testIfSessionIsClosedToNewOnes() {
        String code = "0x123F";
        ServerSession currentServerSession = ServerSession.builder()
            .code(code)
            .state(SessionState.QUARANTINED)
            .build();

        SessionCode sessionCode = SessionCode.builder().hexadecimal(code).build();
        when(sessionRepository.findByCode(code)).thenReturn(Optional.of(currentServerSession));

        assertThrows(SessionNotAvailableException.class,
            () -> sessionController.byCode(sessionCode));
    }

    @Test
    public void testIfSessionCodeIsHexadecimal() {
        SessionCode code = SessionCode.builder().hexadecimal("abwz").build();

        assertThrows(SessionCodeNotHexadecimalException.class,
            () -> sessionController.byCode(code));
    }

    @Test
    public void testIfSessionCodeBeginsWith0x() {
        SessionCode code = SessionCode.builder().hexadecimal("11FE").build();

        assertThrows(SessionCodeNotHexadecimalException.class,
            () -> sessionController.byCode(code));
    }

    @Test
    public void testThrowsIfPollDoesntExist() {
        int idMod = 1;
        int idPoll = 5;
        String token = "habababa";
        Moderator moderator = new Moderator();
        ClientSession clientSession = ClientSession.builder().build();

        when(moderatorRepository.findById(idMod)).thenReturn(Optional.of(moderator));
        when(moderatorRepository.findByToken(token)).thenReturn(Optional.of(moderator));

        when(serverPollRepository.findByModeratorAndId(moderator, idPoll))
            .thenReturn(Optional.empty());

        assertThrows(PollNotExistingException.class,
            () -> sessionController.putSession(idMod, idPoll, token, clientSession));
    }

    @Test
    public void testThrowsIfBadToken() {
        int idMod = 1;
        int idPoll = 5;
        String token = "habababa";
        Moderator moderator = new Moderator();
        ClientSession clientSession = ClientSession.builder().build();

        when(moderatorRepository.findById(idMod)).thenReturn(Optional.of(moderator));
        when(moderatorRepository.findByToken(token)).thenReturn(Optional.empty());

        assertThrows(WrongCredentialsException.class,
            () -> sessionController.putSession(idMod, idPoll, token, clientSession));
    }

    @Test
    public void testThrowsIfBadIdMod() {
        int idMod = 1;
        int idPoll = 5;
        String token = "habababa";
        ClientSession clientSession = ClientSession.builder().build();

        when(moderatorRepository.findById(idMod)).thenReturn(Optional.empty());

        assertThrows(WrongCredentialsException.class,
            () -> sessionController.putSession(idMod, idPoll, token, clientSession));
    }

    @Test
    public void testGetLastActiveSessionThrowsIfNoSession() {
        int idMod = 1;
        int idPoll = 5;
        String token = "habababa";
        Moderator moderator = Moderator.builder()
            .idModerator(idMod)
            .build();

        ServerPoll serverPoll = ServerPoll.builder()
            .idPoll(ServerPollIdentifier.builder()
                .idPoll(idPoll)
                .idxModerator(moderator)
                .build())
            .title("My poll")
            .build();

        when(moderatorRepository.findById(idMod)).thenReturn(Optional.of(moderator));
        when(moderatorRepository.findByToken(token)).thenReturn(Optional.of(moderator));
        when(serverPollRepository.findByModeratorAndId(moderator, idPoll)).thenReturn(
            Optional.of(serverPoll)
        );

        assertThrows(SessionNotExistingException.class,
            () -> sessionController.getLastActiveSession(idMod, idPoll, token));
    }

    @Test
    public void testGetLastActiveSessionWithOneSessionReturnsCorrectSession()
        throws WrongCredentialsException, SessionNotExistingException, PollNotExistingException {
        int idMod = 1;
        int idPoll = 5;
        String token = "habababa";
        Moderator moderator = Moderator.builder()
            .idModerator(idMod)
            .build();

        ServerPoll serverPoll = ServerPoll.builder()
            .idPoll(ServerPollIdentifier.builder()
                .idPoll(idPoll)
                .idxModerator(moderator)
                .build())
            .title("My poll")
            .build();

        ServerSession serverSession = ServerSession.builder().build();

        when(moderatorRepository.findById(idMod)).thenReturn(Optional.of(moderator));
        when(moderatorRepository.findByToken(token)).thenReturn(Optional.of(moderator));
        when(serverPollRepository.findByModeratorAndId(moderator, idPoll)).thenReturn(
            Optional.of(serverPoll)
        );
        when(sessionRepository.findByModAndPoll(moderator, serverPoll)).thenReturn(
            Collections.singletonList(serverSession)
        );

        assertEquals(serverSession, sessionController.getLastActiveSession(idMod, idPoll, token));
    }
}
