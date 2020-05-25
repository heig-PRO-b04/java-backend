package ch.heigvd.pro.b04.tea;

import ch.heigvd.pro.b04.messages.ServerMessage;
import ch.heigvd.pro.b04.tea.exceptions.TeapotException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeaController {

  /**
   * If coffee is asked, answer that we cannot !
   * https://tools.ietf.org/html/rfc2324#section-2.3.2
   * @throws TeapotException Always
   */
  @GetMapping("/coffee")
  public void makeCoffee()
      throws TeapotException {
    throw new TeapotException();
  }

  /**
   * If we cannot brew coffee, we can make Tea.
   * @return A T
   */
  @GetMapping("/tea")
  public ServerMessage makeTea() {
    return ServerMessage.builder()
        .message("T")
        .build();
  }
}
