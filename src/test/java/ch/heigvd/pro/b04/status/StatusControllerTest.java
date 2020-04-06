package ch.heigvd.pro.b04.status;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StatusControllerTest {

  @InjectMocks
  private StatusController controller;

  @Test
  public void testStatusRespondsNormally() {
    assertEquals("Everything is fine.", controller.status().getMessage());
  }
}
