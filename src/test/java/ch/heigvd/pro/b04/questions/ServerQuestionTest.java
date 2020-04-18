package ch.heigvd.pro.b04.questions;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.heigvd.pro.b04.auth.exceptions.WrongCredentialsException;
import ch.heigvd.pro.b04.error.exceptions.ResourceNotFoundException;
import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorRepository;
import ch.heigvd.pro.b04.polls.ServerPoll;
import ch.heigvd.pro.b04.polls.ServerPollRepository;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServerQuestionTest {

  @InjectMocks
  QuestionController cc;
  @Mock
  ServerPollRepository pollRepo;

  @Mock
  QuestionRepository repo;

  @Mock
  ModeratorRepository modoRepo;

  @Test
  public void testModeratorCannotAccessQuestionsOfOtherModerators()
      throws ResourceNotFoundException {
    ServerPoll pollTest=new ServerPoll();

    Moderator aayla = Moderator.builder()
        .idModerator(1)
        .username("aayla")
        .secret("ryloth")
        .pollSet(new Set<ServerPoll>() {
          @Override
          public int size() {
            return 0;
          }

          @Override
          public boolean isEmpty() {
            return true;
          }

          @Override
          public boolean contains(Object o) {
            return false;
          }

          @Override
          public Iterator<ServerPoll> iterator() {
            return null;
          }

          @Override
          public Object[] toArray() {
            return new Object[0];
          }

          @Override
          public <T> T[] toArray(T[] a) {
            return null;
          }

          @Override
          public boolean add(ServerPoll serverPoll) {
            return false;
          }

          @Override
          public boolean remove(Object o) {
            return false;
          }

          @Override
          public boolean containsAll(Collection<?> c) {
            return false;
          }

          @Override
          public boolean addAll(Collection<? extends ServerPoll> c) {
            return false;
          }

          @Override
          public boolean retainAll(Collection<?> c) {
            return false;
          }

          @Override
          public boolean removeAll(Collection<?> c) {
            return false;
          }

          @Override
          public void clear() {

          }
        })
        .build();

    Moderator talon=Moderator.builder()
        .idModerator(2)
        .username("talon")
        .secret("sith").build();

    Mockito.when(aayla.searchPoll(Mockito.any())).thenReturn(pollTest);
    Mockito.when(talon.searchPoll(Mockito.any())).thenReturn(null);
    Mockito.when(modoRepo.findByToken("t1")).thenReturn(Optional.of(aayla));
    Mockito.when((modoRepo.findByToken("t2"))).thenReturn(Optional.of(talon));

    assertDoesNotThrow(() -> cc.all(pollTest.getIdPoll(),"t1",1));
  }
}
