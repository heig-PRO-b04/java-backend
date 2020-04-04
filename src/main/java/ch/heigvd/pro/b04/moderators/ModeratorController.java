package ch.heigvd.pro.b04.moderators;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModeratorController {

  @Autowired
  private ModeratorRepository repository;

  public ModeratorController(ModeratorRepository repository) {
    this.repository = repository;
  }

  @RequestMapping(value = "/moderator", method = RequestMethod.GET)
  List<Moderator> all() {
    return repository.findAll();
  }

  @RequestMapping(value = "/moderator/{id}", method = RequestMethod.GET)
  public Moderator byId(@PathVariable Integer id) {
    return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found."));
  }
}
