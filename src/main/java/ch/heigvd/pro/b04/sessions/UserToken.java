package ch.heigvd.pro.b04.sessions;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.jackson.JsonComponent;

/**
 * A class representing a Token, concerning users.
 */
@Builder
@Data
@AllArgsConstructor
public class UserToken {
  private String token;

  @JsonComponent
  public static class UserTokenJsonSerializer extends
      JsonSerializer<UserToken> {

    @Override
    public void serialize(
        UserToken userToken,
        JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider
    ) throws IOException {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeStringField("token", userToken.token);
      jsonGenerator.writeEndObject();
    }
  }
}
