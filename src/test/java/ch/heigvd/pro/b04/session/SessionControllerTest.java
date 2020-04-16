package ch.heigvd.pro.b04.session;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import ch.heigvd.pro.b04.sessions.Session;
import ch.heigvd.pro.b04.sessions.SessionCode;
import ch.heigvd.pro.b04.sessions.SessionController;
import ch.heigvd.pro.b04.sessions.SessionRepository;
import ch.heigvd.pro.b04.sessions.exceptions.SessionCodeNotHexadecimalException;
import ch.heigvd.pro.b04.sessions.exceptions.SessionNotAvailableException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {
    @InjectMocks
    SessionController session;

    @Mock
    SessionRepository repository;

    @Test
    public void testIfSessionIsClosed() {
        String code = "0x123F";
        Session currentSession = new Session(123);
        currentSession.setCode(code);
        currentSession.setState(Session.SessionState.CLOSED);

        SessionCode sessionCode = SessionCode.builder().hexadecimal(code).build();
        when(repository.findByCode(code)).thenReturn(Optional.of(currentSession));

        assertThrows(SessionNotAvailableException.class, () -> session.byCode(sessionCode));
    }

    @Test
    public void testIfSessionIsClosedToNewOnes() {
        String code = "0x123F";
        Session currentSession = new Session(123);
        currentSession.setCode(code);
        currentSession.setState(Session.SessionState.CLOSED_TO_NEW_ONES);

        SessionCode sessionCode = SessionCode.builder().hexadecimal(code).build();
        when(repository.findByCode(code)).thenReturn(Optional.of(currentSession));

        assertThrows(SessionNotAvailableException.class, () -> session.byCode(sessionCode));
    }

    @Test
    public void testIfSessionCodeIsHexadecimal() {
        Session currentSession = new Session(123);

        SessionCode code = SessionCode.builder().hexadecimal("abwz").build();

        assertThrows(SessionCodeNotHexadecimalException.class, () -> session.byCode(code));
    }

    @Test
    public void testIfSessionCodeBeginsWith0x() {
        Session currentSession = new Session(123);

        SessionCode code = SessionCode.builder().hexadecimal("11FE").build();

        assertThrows(SessionCodeNotHexadecimalException.class, () -> session.byCode(code));
    }
}
