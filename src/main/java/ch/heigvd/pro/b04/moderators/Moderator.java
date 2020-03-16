package ch.heigvd.pro.b04.moderators;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Moderator {

  @GeneratedValue
  @Id
  private Long id;
  private String name;
  private String secret;

  public Moderator() {
  }

  public Moderator(String name, String secret) {
    this.name = name;
    this.secret = secret;
  }
}
