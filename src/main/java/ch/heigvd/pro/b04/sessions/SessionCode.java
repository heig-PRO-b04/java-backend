package ch.heigvd.pro.b04.sessions;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SessionCode {

  private String hexadecimal;
}
