package ch.heigvd.pro.b04.auth;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenCredentials {

  private String token;
}
