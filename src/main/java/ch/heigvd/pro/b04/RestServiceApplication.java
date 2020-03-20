package ch.heigvd.pro.b04;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestServiceApplication {

  /**
   * The main entry point of the application.
   * @param args Command-line arguments, passed when the app is run.
   */
  public static void main(String[] args) {
    System.err.println("Running on " + System.getenv("SPRING_DATASOURCE_URL"));
    System.err.println("Username : " + System.getenv("SPRING_DATASOURCE_USERNAME"));
    System.err.println("Password : " + System.getenv("SPRING_DATASOURCE_PASSWORD"));
    SpringApplication.run(RestServiceApplication.class, args);
  }
}
