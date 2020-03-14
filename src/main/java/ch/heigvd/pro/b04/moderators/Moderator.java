package ch.heigvd.pro.b04.moderators;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Moderator
{

    @GeneratedValue @Id private Long id;
    private String secret;

    public Moderator()
    {}

    public Moderator(String secret)
    {
        this.secret = secret;
    }
}
