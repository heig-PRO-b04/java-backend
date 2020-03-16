package ch.heigvd.pro.b04.questions;

import ch.heigvd.pro.b04.moderators.Moderator;
import ch.heigvd.pro.b04.moderators.ModeratorController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

public class QuestionController {
    private QuestionRepository repository;

    public QuestionController(QuestionRepository repo) {repository=repo;}

    @RequestMapping(value = "/question", method = RequestMethod.GET)
    List<Question> all() {
        return repository.findAll();
    }

    @RequestMapping(value = "/question/{id}", method = RequestMethod.GET)
    Question byId(@PathVariable QuestionIdentifier id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found."));
    }
}
