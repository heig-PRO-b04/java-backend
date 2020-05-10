package ch.heigvd.pro.b04.statistics;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.springframework.boot.jackson.JsonComponent;

@Builder
public class ServerQuestionStatistics {

  @Singular
  @Getter
  private final List<Answer> answers;

  @Singular
  @Getter
  private final List<Timestamp> timestamps;

  @Builder
  public static class Answer {

    @Getter
    private final int id;
    @Getter
    private final String title;

    @JsonComponent
    public static class Serializer extends JsonSerializer<Answer> {

      @Override
      public void serialize(
          Answer answer,
          JsonGenerator jsonGenerator,
          SerializerProvider serializerProvider
      ) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("idAnswer", answer.id);
        jsonGenerator.writeStringField("title", answer.title);
        jsonGenerator.writeEndObject();
      }
    }
  }

  @Builder
  public static class Timestamp {

    @Getter
    private final int seconds;
    @Singular
    @Getter
    private final List<VoteCount> votes;

    @JsonComponent
    public static class Serializer extends JsonSerializer<Timestamp> {


      @Override
      public void serialize(
          Timestamp timestamp,
          JsonGenerator jsonGenerator,
          SerializerProvider serializerProvider
      ) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("seconds", timestamp.seconds);
        jsonGenerator.writeObjectField("votes", timestamp.votes);
        jsonGenerator.writeEndObject();
      }
    }
  }

  @Builder
  public static class VoteCount {

    @Getter
    private final int idAnswer;
    @Getter
    private final int count;

    @JsonComponent
    public static class Serializer extends JsonSerializer<VoteCount> {

      @Override
      public void serialize(
          VoteCount voteCount,
          JsonGenerator jsonGenerator,
          SerializerProvider serializerProvider
      ) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("idAnswer", voteCount.idAnswer);
        jsonGenerator.writeNumberField("count", voteCount.count);
        jsonGenerator.writeEndObject();
      }
    }
  }

  @JsonComponent
  public static class Serializer extends JsonSerializer<ServerQuestionStatistics> {

    @Override
    public void serialize(
        ServerQuestionStatistics serverQuestionStatistics,
        JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider
    ) throws IOException {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeObjectField("answers", serverQuestionStatistics.answers);
      jsonGenerator.writeObjectField("timestamps", serverQuestionStatistics.timestamps);
      jsonGenerator.writeEndObject();
    }
  }
}
