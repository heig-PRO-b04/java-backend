package ch.heigvd.pro.b04.account;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.jackson.JsonComponent;

@Builder
@Data
public class NewPassword {

  private String currentPassword;
  private String newPassword;

  @JsonComponent
  public static class Deserializer extends JsonDeserializer<NewPassword> {

    @Override
    public NewPassword deserialize(
        JsonParser jsonParser,
        DeserializationContext deserializationContext
    ) throws IOException {
      JsonNode node = jsonParser.getCodec().readTree(jsonParser);
      String current = node.get("currentPassword").asText();
      String next = node.get("newPassword").asText();
      return NewPassword.builder()
          .currentPassword(current)
          .newPassword(next)
          .build();
    }
  }
}
