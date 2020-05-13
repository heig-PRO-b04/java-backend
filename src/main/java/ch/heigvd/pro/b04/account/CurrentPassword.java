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
public class CurrentPassword {

  private String currentPassword;

  @JsonComponent
  public static class Deserializer extends JsonDeserializer<CurrentPassword> {

    @Override
    public CurrentPassword deserialize(
        JsonParser jsonParser,
        DeserializationContext deserializationContext
    ) throws IOException {
      JsonNode node = jsonParser.getCodec().readTree(jsonParser);
      String current = node.get("currentPassword").asText();
      return CurrentPassword.builder()
          .currentPassword(current)
          .build();
    }
  }
}
