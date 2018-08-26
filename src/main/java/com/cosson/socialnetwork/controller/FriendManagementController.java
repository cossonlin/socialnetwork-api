package com.cosson.socialnetwork.controller;

import com.cosson.socialnetwork.service.FriendManagementService;
import com.cosson.socialnetwork.request.ListOfFriends;
import com.cosson.socialnetwork.request.PairUpdate;
import com.cosson.socialnetwork.request.RetrieveFriendList;
import com.cosson.socialnetwork.request.UserUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/friend")
public class FriendManagementController {

    @Autowired
    private FriendManagementService service;

    //As a user, I need an API to create a friend connection between two email addresses.
    @PostMapping(path = "/createConnection", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createConnection(@RequestBody ListOfFriends requestBody) {
        Object result = service.createConnection(requestBody.getFriends());
        return ResponseEntity.ok(result);
    }

    //As a user, I need an API to retrieve the friends list for an email address.
    @PostMapping(path = "/retrieveFriendList", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> retrieveFriendList(@RequestBody RetrieveFriendList requestBody) {
        Object result = service.retrieveFriendListResult(requestBody.getEmail());
        return ResponseEntity.ok(result);
    }

    //As a user, I need an API to retrieve the common friends list between two email addresses.
    @PostMapping(path = "/retrieveCommonFriendList", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> retrieveCommonFriendList(@RequestBody ListOfFriends requestBody) {
        Object result = service.retrieveCommonFriendList(requestBody.getFriends());
        return ResponseEntity.ok(result);
    }

    //As a user, I need an API to subscribe to updates from an email address.
    @PostMapping(path = "/subscribeUpdate", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> subscribeUpdate(@RequestBody PairUpdate requestBody) {
        Object result = service.subscribeUpdate(requestBody.getRequestor(), requestBody.getTarget());
        return ResponseEntity.ok(result);
    }

    //As a user, I need an API to block updates from an email address.
    @PostMapping(path = "/blockUpdates", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> blockUpdates(@RequestBody PairUpdate requestBody) {
        Object result = service.blockUpdates(requestBody.getRequestor(), requestBody.getTarget());
        return ResponseEntity.ok(result);
    }

    //As a user, I need an API to retrieve all email addresses that can receive updates from an email address.
    @PostMapping(path = "/retrieveEligibleSubscriber", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> retrieveEligibleSubscriber(@RequestBody UserUpdate requestBody) {
        Object result = service.retrieveEligibleSubscriber(requestBody.getSender(), requestBody.getText());
        return ResponseEntity.ok(result);
    }
}
