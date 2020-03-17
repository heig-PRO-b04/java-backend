package ch.heigvd.pro.b04;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RestServiceApplication {

    public static void main(String[] args) {
    SpringApplication.run(RestServiceApplication.class, args);
  }
}
