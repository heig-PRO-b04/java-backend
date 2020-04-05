package ch.heigvd.pro.b04.status;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

  @GetMapping("/")
  public Status status() {
    return new Status("Everything is fine.");
  }
}
