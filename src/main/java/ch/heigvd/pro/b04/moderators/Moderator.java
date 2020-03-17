package ch.heigvd.pro.b04.moderators;

import javax.persistence.Table;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
//@Table(name="moderator")
public class Moderator {

  @Id private String idModerator;
  private String secret;

  public Moderator() {}

  public Moderator(String name, String secret) {
    this.idModerator = name;
    this.secret = secret;
  }
}
