package ch.heigvd.pro.b04.question;

import lombok.Data;
import java.utils.*;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Data
@Entity //but not Tina Turner
public class Question
{
    @EmbeddedId private  QuestionIdentifier id;
    private String title;
}