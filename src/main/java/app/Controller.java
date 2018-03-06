package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@RestController
public class Controller {

    @Autowired
    private UserRepository repo;


    @PostConstruct
    public void init() {
        repo.save(new User("mkukulic"));
        repo.save(new User("ACiganj"));
        repo.save(new User("BEST_Zagreb"));
    }

    @RequestMapping("/")
    public Iterable<User> users() {
       return repo.findAll();
    }
}
