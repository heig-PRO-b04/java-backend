package ch.heigvd.pro.b04;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    // We're an open API.
    registry.addMapping("/**").allowedMethods("DELETE", "GET", "POST", "PUT");
  }
}
