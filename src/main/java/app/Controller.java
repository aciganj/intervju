package app;

import app.model.User;
import app.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
public class Controller {

    @Autowired
    private UserRepository repo;

    @Autowired
    Environment env;

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

    @RequestMapping("/twitter")
    public Set<String> twitterFollowers() {
        String appId = env.getProperty("spring.social.twitter.appId");
        String appSecret = env.getProperty("spring.social.twitter.appSecret");
        String appToken = fetchApplicationAccessToken(appId, appSecret);

        return getFollowers(appToken);
    }



    private static final String url = "https://api.twitter.com/1.1/followers/list.json?cursor=-1&screen_name=ACiganj&skip_status=true&include_user_entities=false";



    public static Set<String> getFollowers(String appToken) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + appToken);
        HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
        Map<String, ?> result = rest.exchange(url, HttpMethod.GET, requestEntity, Map.class).getBody();
        List<Map<String, ?>> userItems = (List<Map<String, ?>>) result.get("users");
        Set<String> users = new HashSet<>();
        for (Map<String, ?> userItem : userItems) {
            users.add(userItem.get("name").toString());
        }
        return users;
    }

    public static String fetchApplicationAccessToken(String appId, String appSecret) {
        // Twitter supports OAuth2 *only* for obtaining an application token, not for user tokens.
        OAuth2Operations oauth = new OAuth2Template(appId, appSecret, "", "https://api.twitter.com/oauth2/token");
        return oauth.authenticateClient().getAccessToken();
    }












}
