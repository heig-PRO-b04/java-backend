package ch.heigvd.pro.b04.answers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class AnswerController {

  @Autowired
  private AnswerRepository repository;

  public AnswerController(AnswerRepository repo) {
    repository = repo;
  }

  @RequestMapping(value = "/answer", method = RequestMethod.GET)
  List<Answer> all() {
    return repository.findAll();
  }

  @RequestMapping(value = "/answer/{id}", method = RequestMethod.GET)
  Answer byId(@PathVariable AnswerIdentifier id) {
    return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found."));
  }
}
