package com.github.coderoute.users;

import com.github.coderoute.Main;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Main.class)
@WebIntegrationTest("server.port:19000")
public class UsersControllerIntegrationTest {

    public static final String TEST_USER_NAME = "alice";
    RestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void testUserCreateGetAndDelete() {

        String createdUserUri = checkCreateUser();
        User fetchedUser = checkGetUser(createdUserUri);

        ResponseEntity<String> findAllResponse = restTemplate.getForEntity("http://localhost:19000/users", String.class);
        assertThat(findAllResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(findAllResponse.getBody(), containsString("{\"uuid\":\"" +fetchedUser.getUuid() + "\",\"name\":\"alice\"}"));

        restTemplate.delete(createdUserUri);

        checkUserIsNotFound(createdUserUri);
    }

    private User checkGetUser(String createdUserUri) {
        ResponseEntity<User> getResponseEntity = restTemplate.getForEntity(createdUserUri, User.class);
        User fetchedUser = getResponseEntity.getBody();
        assertThat(fetchedUser, notNullValue());
        assertThat(fetchedUser.getName(), is(TEST_USER_NAME));
        assertThat(fetchedUser.getUuid(), notNullValue());
        assertThat(getResponseEntity.getStatusCode(), is(HttpStatus.OK));
        return fetchedUser;
    }

    private String checkCreateUser() {
        User user = new User();
        user.setName(TEST_USER_NAME);
        ResponseEntity<User> createResponseEntity = restTemplate.postForEntity("http://localhost:19000/users", user, User.class);
        User createdUser = createResponseEntity.getBody();
        assertThat(createdUser, notNullValue());
        assertThat(createdUser.getName(), is(TEST_USER_NAME));
        assertThat(createdUser.getUuid(), notNullValue());
        assertThat(createResponseEntity.getStatusCode(), is(HttpStatus.CREATED));

        String createdUserUri = createResponseEntity.getHeaders().getLocation().toString();
        assertThat(createdUserUri, startsWith("http://localhost:19000/users/"));
        return createdUserUri;
    }

    private void checkUserIsNotFound(String userResourceUri) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(userResourceUri, String.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

}