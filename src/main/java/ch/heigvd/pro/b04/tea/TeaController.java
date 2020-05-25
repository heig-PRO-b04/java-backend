package ch.heigvd.pro.b04.tea;

import ch.heigvd.pro.b04.messages.ServerMessage;
import ch.heigvd.pro.b04.tea.exceptions.TeapotException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeaController {

  @GetMapping("/coffee")
  public void makeCoffee()
      throws TeapotException {
    throw new TeapotException();
  }

  @GetMapping("/tea")
  public ServerMessage makeTea() {
    return ServerMessage.builder()
        .message("T")
        .build();
  }
}
