package com.cosson;

import com.cosson.socialnetwork.controller.FriendManagementController;
import com.cosson.socialnetwork.response.FriendListResult;
import com.cosson.socialnetwork.response.SimpleResult;
import com.cosson.socialnetwork.response.SubscriberResult;
import com.cosson.socialnetwork.service.FriendManagementService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebMvcTest(value = FriendManagementController.class, secure = false)
public class ApiServerApplicationTest {

    private final String createConnectionUrl = "/friend/createConnection";
    private final String retrieveFriendUrl = "/friend/retrieveFriendList";
    private final String retrieveCommonFriendUrl = "/friend/retrieveCommonFriendList";
    private final String subscribeUpdateUrl = "/friend/subscribeUpdate";
    private final String blockUpdatesUrl = "/friend/blockUpdates";
    private final String retrieveEligibleSubscriberUrl = "/friend/retrieveEligibleSubscriber";

    private final SimpleResult mockSimpleResult = new SimpleResult(true);
    private final String simpleResultJson = "{\"success\":true}";
    private final String mockFriendListResultJson = "{\"success\":true,\"friends\":[\"test1@test.com\",\"test2@test.com\"],\"count\":2}";
    private final String exampleListOfFriendsJson = "{\n" +
            "  \"friends\":\n" +
            "    [\n" +
            "      \"abc@test.com\",\n" +
            "      \"bcd@test.com\"\n" +
            "    ]\n" +
            "}";
    private final String requestorTargetJson = "{\n" +
            "  \"requestor\": \"requestor@test.com\",\n" +
            "  \"target\": \"target@test.com\"\n" +
            "}";
    private FriendListResult mockFriendListResult;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FriendManagementService service;

    @Before
    public void before() {
        List<String> friends = new ArrayList<>();
        friends.add("test1@test.com");
        friends.add("test2@test.com");
        mockFriendListResult = new FriendListResult(true, friends);
    }

    @Test
    public void testCreateConnection() throws Exception {
        Mockito.when(
                service.createConnection(Mockito.anyList())).thenReturn(mockSimpleResult);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(createConnectionUrl)
                .accept(MediaType.APPLICATION_JSON)
                .content(exampleListOfFriendsJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertEquals(simpleResultJson, result.getResponse().getContentAsString());
    }

    @Test
    public void testRetrieveFriendList() throws Exception {
        Mockito.when(
                service.retrieveFriendListResult(Mockito.anyString())).thenReturn(mockFriendListResult);

        String requestContent = "{\n" +
                "  \"email\": \"anyone@test.com\"\n" +
                "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(retrieveFriendUrl)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestContent)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(mockFriendListResultJson, result.getResponse().getContentAsString());
    }

    @Test
    public void testRetrieveCommonFriendList() throws Exception {
        Mockito.when(
                service.retrieveCommonFriendList(Mockito.anyList())).thenReturn(mockFriendListResult);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(retrieveCommonFriendUrl)
                .accept(MediaType.APPLICATION_JSON)
                .content(exampleListOfFriendsJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(mockFriendListResultJson, result.getResponse().getContentAsString());
    }

    @Test
    public void testSubscribeUpdate() throws Exception {
        Mockito.when(
                service.subscribeUpdate("requestor@test.com", "target@test.com")).thenReturn(mockSimpleResult);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(subscribeUpdateUrl)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestorTargetJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertEquals(simpleResultJson, result.getResponse().getContentAsString());
    }

    @Test
    public void testBlockUpdates() throws Exception {
        Mockito.when(
                service.blockUpdates("requestor@test.com", "target@test.com")).thenReturn(mockSimpleResult);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(blockUpdatesUrl)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestorTargetJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertEquals(simpleResultJson, result.getResponse().getContentAsString());
    }

    @Test
    public void testRetrieveEligibleSubscriber() throws Exception {
        List<String> recipients = new ArrayList<>();
        recipients.add("atUser@test.com");
        recipients.add("friend@test.com");
        SubscriberResult mockSubcriberResult = new SubscriberResult(true, recipients);

        Mockito.when(
                service.retrieveEligibleSubscriber("sender@test.com", "Hey, atUser@test.com")).thenReturn(mockSubcriberResult);

        String testEligibleSubcriberJson = "{\n" +
                "  \"sender\":  \"sender@test.com\",\n" +
                "  \"text\": \"Hey, atUser@test.com\"\n" +
                "}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(retrieveEligibleSubscriberUrl)
                .accept(MediaType.APPLICATION_JSON)
                .content(testEligibleSubcriberJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"success\":true,\"recipients\":[\"atUser@test.com\",\"friend@test.com\"]}";
        assertEquals(expected, result.getResponse().getContentAsString());
    }
}
