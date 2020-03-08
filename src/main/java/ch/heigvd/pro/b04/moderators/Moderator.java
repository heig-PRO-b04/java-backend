package ch.heigvd.pro.b04.moderators;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Moderator {

  @GeneratedValue @Id private Long id;
  private String name;
  private String secret;

  public Moderator() {}

  public Moderator(String name, String secret) {
    this.name = name;
    this.secret = secret;
  }
}
