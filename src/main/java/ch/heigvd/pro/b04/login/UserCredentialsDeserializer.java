package ch.heigvd.pro.b04.login;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
class UserCredentialsDeserializer extends JsonDeserializer<UserCredentials> {

  @Override
  public UserCredentials deserialize(
      JsonParser jsonParser,
      DeserializationContext deserializationContext
  ) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    String username = node.get("username").asText();
    String password = node.get("password").asText();
    return UserCredentials.builder()
        .username(username)
        .password(password)
        .build();
  }
}
