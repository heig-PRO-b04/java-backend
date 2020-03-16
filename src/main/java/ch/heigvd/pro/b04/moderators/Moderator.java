package ch.heigvd.pro.b04.moderators;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Moderator {

  @GeneratedValue @Id private String idModerator;
  private String secret;

  public Moderator() {}

  public Moderator(String name, String secret) {
    this.idModerator = name;
    this.secret = secret;
  }
}
