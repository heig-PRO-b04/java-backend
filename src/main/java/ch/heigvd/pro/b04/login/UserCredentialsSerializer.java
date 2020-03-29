package ch.heigvd.pro.b04.login;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class UserCredentialsSerializer extends JsonSerializer<UserCredentials> {

  @Override
  public void serialize(
      UserCredentials userCredentials,
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider
  ) throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeStringField("username", userCredentials.getUsername());
    jsonGenerator.writeStringField("password", userCredentials.getPassword());
    jsonGenerator.writeEndObject();
  }
}

