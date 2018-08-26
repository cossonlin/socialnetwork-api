package com.cosson;

import com.cosson.socialnetwork.request.ListOfFriends;
import com.cosson.socialnetwork.request.PairUpdate;
import com.cosson.socialnetwork.request.RetrieveFriendList;
import com.cosson.socialnetwork.request.UserUpdate;
import com.cosson.socialnetwork.response.FriendListResult;
import com.cosson.socialnetwork.response.SimpleResult;
import com.cosson.socialnetwork.response.SubscriberResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiServerApplicationTest {

    private final String apiUrl = "http://localhost:8080/friend/";
    private final String createConnectionUrl = apiUrl + "createConnection";
    private final String retrieveFriendUrl = apiUrl + "retrieveFriendList";
    private final String retrieveCommonFriendUrl = apiUrl + "retrieveCommonFriendList";
    private final String subscribeUpdateUrl = apiUrl + "subscribeUpdate";
    private final String blockUpdatesUrl = apiUrl + "blockUpdates";
    private final String retrieveEligibleSubscriberUrl = apiUrl + "retrieveEligibleSubscriber";
    TestRestTemplate template = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @Before
    public void before() {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void testCreateConnection() {
        List<String> friendList = new ArrayList<>();
        friendList.add("test11@test.com");
        friendList.add("test12@test.com");
        ListOfFriends listOfFriends = new ListOfFriends(friendList);
        HttpEntity<ListOfFriends> entity = new HttpEntity<>(listOfFriends, headers);
        SimpleResult response = template.postForObject(createConnectionUrl, entity, SimpleResult.class);
        assertTrue(response.isSuccess() == true);
    }

    @Test
    public void testRetrieveFriendList() {
        List<String> friendList = new ArrayList<>();
        friendList.add("test21@test.com");
        friendList.add("test22@test.com");
        ListOfFriends listOfFriends = new ListOfFriends(friendList);
        HttpEntity<ListOfFriends> entity = new HttpEntity<>(listOfFriends, headers);
        template.postForObject(createConnectionUrl, entity, SimpleResult.class);

        friendList.remove(1);
        friendList.add("test23@test.com");
        listOfFriends = new ListOfFriends(friendList);
        entity = new HttpEntity<>(listOfFriends, headers);
        template.postForObject(createConnectionUrl, entity, SimpleResult.class);

        RetrieveFriendList retrieveFriendList = new RetrieveFriendList("test21@test.com");
        HttpEntity<RetrieveFriendList> entity1 = new HttpEntity<>(retrieveFriendList, headers);
        FriendListResult response = template.postForObject(retrieveFriendUrl, entity1, FriendListResult.class);
        assertTrue(response.isSuccess() == true);
        assertTrue(response.getCount() == 2);
        assertTrue(response.getFriends().contains("test22@test.com"));
    }

    @Test
    public void testRetrieveCommonFriendList() throws Exception {
        List<String> friendList = new ArrayList<>();
        friendList.add("test31@test.com");
        friendList.add("test32@test.com");
        ListOfFriends listOfFriends = new ListOfFriends(friendList);
        HttpEntity<ListOfFriends> entity = new HttpEntity<>(listOfFriends, headers);
        template.postForObject(createConnectionUrl, entity, SimpleResult.class);

        friendList.remove(1);
        friendList.add("test33@test.com");
        listOfFriends = new ListOfFriends(friendList);
        entity = new HttpEntity<>(listOfFriends, headers);
        template.postForObject(createConnectionUrl, entity, SimpleResult.class);

        friendList.clear();
        friendList.add("test32@test.com");
        friendList.add("test33@test.com");
        ListOfFriends queryList = new ListOfFriends(friendList);
        entity = new HttpEntity<>(queryList, headers);
        FriendListResult response = template.postForObject(retrieveCommonFriendUrl, entity, FriendListResult.class);
        assertTrue(response.isSuccess() == true);
        assertTrue(response.getFriends().contains("test31@test.com"));
    }

    @Test
    public void testSubscribeUpdate() throws Exception {
        PairUpdate pairUpdate = new PairUpdate("test41@test.com", "test42@test.com");
        HttpEntity<PairUpdate> entity = new HttpEntity<>(pairUpdate, headers);
        SimpleResult response = template.postForObject(subscribeUpdateUrl, entity, SimpleResult.class);
        assertTrue(response.isSuccess() == true);
    }

    @Test
    public void testBlockUpdates() throws Exception {
        PairUpdate pairUpdate = new PairUpdate("test51@test.com", "test52@test.com");
        HttpEntity<PairUpdate> entity = new HttpEntity<>(pairUpdate, headers);
        SimpleResult response = template.postForObject(blockUpdatesUrl, entity, SimpleResult.class);
        assertTrue(response.isSuccess() == true);
    }

    @Test
    public void testRetrieveEligibleSubscriber() throws Exception {
        List<String> friendList = new ArrayList<>();
        friendList.add("test61@test.com");
        friendList.add("test62@test.com");
        ListOfFriends listOfFriends = new ListOfFriends(friendList);
        HttpEntity<ListOfFriends> entity = new HttpEntity<>(listOfFriends, headers);
        template.postForObject(createConnectionUrl, entity, SimpleResult.class);

        friendList.remove(1);
        friendList.add("test63@test.com");
        listOfFriends = new ListOfFriends(friendList);
        entity = new HttpEntity<>(listOfFriends, headers);
        template.postForObject(createConnectionUrl, entity, SimpleResult.class);

        PairUpdate subscriber = new PairUpdate("test64@test.com", "test61@test.com");
        HttpEntity<PairUpdate> subscriberEntity = new HttpEntity<>(subscriber, headers);
        template.postForObject(subscribeUpdateUrl, subscriberEntity, SimpleResult.class);

        PairUpdate blocker = new PairUpdate("test63@test.com", "test61@test.com");
        HttpEntity<PairUpdate> blockerEntity = new HttpEntity<>(blocker, headers);
        template.postForObject(blockUpdatesUrl, blockerEntity, SimpleResult.class);

        PairUpdate blocker2 = new PairUpdate("test65@test.com", "test61@test.com");
        HttpEntity<PairUpdate> blocker2Entity = new HttpEntity<>(blocker2, headers);
        template.postForObject(blockUpdatesUrl, blocker2Entity, SimpleResult.class);

        UserUpdate userUpdate = new UserUpdate("test61@test.com", "Hey test65@test.com and test66@test.com");
        HttpEntity<UserUpdate> userUpdateEntity = new HttpEntity<>(userUpdate, headers);
        SubscriberResult response = template.postForObject(retrieveEligibleSubscriberUrl, userUpdateEntity, SubscriberResult.class);
        assertTrue(response.isSuccess() == true);
        assertTrue(response.getRecipients().size() == 3);
        assertTrue(response.getRecipients().contains("test62@test.com"));
        assertFalse(response.getRecipients().contains("test63@test.com"));
        assertTrue(response.getRecipients().contains("test64@test.com"));
        assertFalse(response.getRecipients().contains("test65@test.com"));
        assertTrue(response.getRecipients().contains("test66@test.com"));
    }
}
