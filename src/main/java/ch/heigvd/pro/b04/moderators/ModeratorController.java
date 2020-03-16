package ch.heigvd.pro.b04.moderators;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ModeratorController {

  private ModeratorRepository repository;

  public ModeratorController(ModeratorRepository repository) {
    this.repository = repository;
  }

  @RequestMapping(value = "/moderator", method = RequestMethod.GET)
  List<Moderator> all() {
    return repository.findAll();
  }

  @RequestMapping(value = "/moderator/{id}", method = RequestMethod.GET)
  Moderator byId(@PathVariable String id) {
    return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found."));
  }
}
