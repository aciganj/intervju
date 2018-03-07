package app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.model.User;
import app.model.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ApplicationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @Test
    public void testFindByFollowing() throws Exception {

        this.entityManager.persist(new User("testUser1",true));
        this.entityManager.persist(new User("testUser2",false));

        List<User> followers = this.repository.findByFollowing(true);
        assertThat(followers.size()).isEqualTo(1);
        assertThat(followers.get(0).getName()).isEqualTo("testUser1");
        assertThat(followers.get(0).isFollowing()).isEqualTo(true);

        List<User> notFollowers = this.repository.findByFollowing(false);
        assertThat(notFollowers.size()).isEqualTo(1);
        assertThat(notFollowers.get(0).getName()).isEqualTo("testUser2");
        assertThat(notFollowers.get(0).isFollowing()).isEqualTo(false);
    }

}
