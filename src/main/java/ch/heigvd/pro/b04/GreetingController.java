package ch.heigvd.pro.b04;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

  private final AtomicLong counter = new AtomicLong();

  @GetMapping("/increment")
  public Counter greeting() {
    return new Counter(counter.incrementAndGet());
  }

  @GetMapping("/decrement")
  public Counter decrement() {
    return new Counter(counter.decrementAndGet());
  }
}
