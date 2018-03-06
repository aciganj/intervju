package app;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class TwitterUtil {

    private static final String url = "https://api.twitter.com/1.1/followers/list.json?cursor=-1&screen_name={username}&skip_status=true&include_user_entities=false";

    private String appToken;

    private TwitterUtil(String appToken){
        this.appToken = appToken;
    }


    /**
     * Fetches handle names of user's followers.
     * @param userHandle handle of the user
     * @return Set of follower handles
     */
    public Set<String> getFollowerHandles(String userHandle) {
        return extractFollowerHandlesToSet(twitterApiFollowersRequest(userHandle));
    }

    private Set<String> extractFollowerHandlesToSet(Map<String,?> responseMap) {

        List<Map<String, ?>> userItems = (List<Map<String, ?>>) responseMap.get("users");
        Set<String> users = new HashSet<>();

        for (Map<String, ?> userItem : userItems) {
            users.add(userItem.get("screen_name").toString());
        }

        return users;
    }

    private Map<String, ?> twitterApiFollowersRequest(String username){
        RestTemplate rest = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + appToken);

        HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);

        return rest.exchange(url, HttpMethod.GET, requestEntity, Map.class, username).getBody();
    }

    /**
     * Factory method which constructs authenticated twitter util object for supplied appId and appSecret.
     * @return new twitter connection
     */
    public static TwitterUtil initialize(String appId, String appSecret){
        String accessToken = fetchApplicationAccessToken(appId,appSecret);
        return new TwitterUtil(accessToken);
    }

    /**
     * Authenticates the application and retrieves access token.
     * @param appId application id (consumer id)
     * @param appSecret application secret (consumer secret)
     * @return access token
     */
    private static String fetchApplicationAccessToken(String appId, String appSecret) {
        OAuth2Operations oauth = new OAuth2Template(appId, appSecret, "", "https://api.twitter.com/oauth2/token");
        return oauth.authenticateClient().getAccessToken();
    }

}