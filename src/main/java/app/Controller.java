package app;

import app.model.User;
import app.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
public class Controller {

    private final static String PREDEFINED_FOLLOWED_USER = "BEST_Zagreb";

    private Timer timer;


    @Autowired
    Environment env;

    TwitterUtil twitterUtil;

    @Autowired
    private UserRepository repo;

    @PostConstruct
    public void init() {


        //initialize twitter util
        String appId = env.getProperty("spring.social.twitter.appId");
        String appSecret = env.getProperty("spring.social.twitter.appSecret");
        twitterUtil = TwitterUtil.initialize(appId,appSecret);

        //fill database with initial users
        repo.save(new User("ACiganj"));
        repo.save(new User("mkukulic"));
        repo.save(new User(PREDEFINED_FOLLOWED_USER));

        //predefined followed user
        monitor(PREDEFINED_FOLLOWED_USER);

    }

    class MyTimerTask extends TimerTask {

        private String userHandle;

        public MyTimerTask(String userHandle) {
            this.userHandle = userHandle;
        }

        @Override
        public void run() {
            updateDatabase(userHandle);
        }

    }

    /**
     * Updates the followers in database with their actual twitter follower status
     * @param handle handle of followed user
     */
    private void updateDatabase(String handle){

        Iterable<User> users = repo.findAll();

        Set<String> followers = twitterUtil.getFollowerHandles(handle);

        for (User user : users) {
            user.setFollowing(followers.contains(user.getName()));
        }
        repo.save(users);

    }

    /**
     * Starts to monitor followers of user with supplied handle.
     * @param handle user handle
     */
    @RequestMapping("/monitor/{handle}")
    public List<User> monitor(@PathVariable("handle") String handle) {
        if (timer != null) {
            timer.cancel();
        }
        //sets up a timer which will start polling after one second
        timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(handle), 1000, 1000);

        //poll immediately so the followers can be returned
        updateDatabase(handle);

        return repo.findByFollowing(true);
    }

    @RequestMapping("/database")
    public List<User> fetchDatabase() {
        return repo.findByFollowing(true);
    }


}