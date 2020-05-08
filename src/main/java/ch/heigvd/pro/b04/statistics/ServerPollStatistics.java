package ch.heigvd.pro.b04.statistics;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Singular;
import org.springframework.boot.jackson.JsonComponent;

@Builder
@Data
public class ServerPollStatistics {

  @Singular
  @Getter
  private final List<QuestionStatistics> questions;

  @Data
  @Builder
  public static class QuestionStatistics {

    @Singular
    private List<AnswerStatistics> answers;
    private String title;

    @JsonComponent
    public static class Serializer extends JsonSerializer<QuestionStatistics> {

      @Override
      public void serialize(
          QuestionStatistics questionStatistics,
          JsonGenerator jsonGenerator,
          SerializerProvider serializerProvider
      ) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("title", questionStatistics.title);
        jsonGenerator.writeObjectField("answers", questionStatistics.answers);
        jsonGenerator.writeEndObject();
      }
    }
  }

  @Data
  @Builder
  public static class AnswerStatistics {

    private int positive;
    private int negative;
    private String title;

    @JsonComponent
    public static class Serializer extends JsonSerializer<AnswerStatistics> {

      @Override
      public void serialize(
          AnswerStatistics answerStatistics,
          JsonGenerator jsonGenerator,
          SerializerProvider serializerProvider
      ) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("positive", answerStatistics.positive);
        jsonGenerator.writeNumberField("negative", answerStatistics.negative);
        jsonGenerator.writeStringField("title", answerStatistics.title);
        jsonGenerator.writeEndObject();
      }
    }
  }

  @JsonComponent
  public static class Serializer extends JsonSerializer<ServerPollStatistics> {

    @Override
    public void serialize(
        ServerPollStatistics statistics,
        JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider
    ) throws IOException {
      jsonGenerator.writeObject(statistics.getQuestions());
    }
  }
}
