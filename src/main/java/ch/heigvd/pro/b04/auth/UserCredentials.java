package ch.heigvd.pro.b04.auth;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserCredentials {

  private String username;
  private String password;
}
