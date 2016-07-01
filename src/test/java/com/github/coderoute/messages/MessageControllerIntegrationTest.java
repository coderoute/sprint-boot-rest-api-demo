package com.github.coderoute.messages;

import com.github.coderoute.Main;
import com.github.coderoute.users.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Main.class)
@WebIntegrationTest("server.port:19000")
public class MessageControllerIntegrationTest {

    public static final String TEST_USER_1_NAME = "alice";
    public static final String TEST_USER_2_NAME = "bob";
    RestTemplate restTemplate = new TestRestTemplate();


    @Test
    public void testMessageCreationAndRetreival() {
        UUID sender = createUser(TEST_USER_1_NAME);
        UUID receiver = createUser(TEST_USER_2_NAME);

        InputMessage inputMessage = new InputMessage();
        inputMessage.setSender(sender);
        inputMessage.setReceiver(receiver);
        inputMessage.setMessage("HELLO");
        ResponseEntity<InputMessage> postResponse = restTemplate.postForEntity("http://localhost:19000/messages", inputMessage, InputMessage.class);
        assertThat(postResponse.getStatusCode(), is(HttpStatus.CREATED));

        ResponseEntity<String> bobMessagesResponse = restTemplate.getForEntity("http://localhost:19000/messages/users/" + receiver, String.class);
        assertThat(bobMessagesResponse.getBody(), containsString("HELLO"));

        restTemplate.delete("http://localhost:19000/users/" + receiver);
        ResponseEntity<String> messageResponseAfterUserDeletion = restTemplate.getForEntity("http://localhost:19000/messages/users/" + receiver, String.class);
        assertThat(messageResponseAfterUserDeletion.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }


    private UUID createUser(String name) {
        User user = new User();
        user.setName(name);
        ResponseEntity<User> createResponseEntity = restTemplate.postForEntity("http://localhost:19000/users", user, User.class);
        User createdUser = createResponseEntity.getBody();
        assertThat(createdUser, notNullValue());
        assertThat(createdUser.getName(), is(name));
        assertThat(createdUser.getUuid(), notNullValue());
        assertThat(createResponseEntity.getStatusCode(), is(HttpStatus.CREATED));
        return createdUser.getUuid();
    }
}
