package ch.heigvd.pro.b04.moderators;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class ModeratorJsonSerializer extends JsonSerializer<Moderator> {

  @Override
  public void serialize(
      Moderator moderator,
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider
  ) throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeStringField("idModerator", moderator.getIdModerator());
    jsonGenerator.writeEndObject();
  }
}
