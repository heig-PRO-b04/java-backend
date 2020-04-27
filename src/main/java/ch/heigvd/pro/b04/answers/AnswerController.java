package ch.heigvd.pro.b04.answers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnswerController {

  @Autowired
  private AnswerRepository repository;

  public AnswerController(AnswerRepository repo) {
    repository = repo;
  }

  @RequestMapping(value = "/answer", method = RequestMethod.GET)
  List<ServerAnswer> all() {
    return repository.findAll();
  }

  @RequestMapping(value = "/answer/{id}", method = RequestMethod.GET)
  ServerAnswer byId(@PathVariable ServerAnswerIdentifier id) {
    return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found."));
  }
}
