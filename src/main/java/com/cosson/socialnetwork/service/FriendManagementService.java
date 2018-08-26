package com.cosson.socialnetwork.service;

import com.cosson.socialnetwork.entity.Block;
import com.cosson.socialnetwork.entity.Friends;
import com.cosson.socialnetwork.entity.Subscription;
import com.cosson.socialnetwork.repository.BlockRepository;
import com.cosson.socialnetwork.repository.FriendRepository;
import com.cosson.socialnetwork.repository.SubscriptionRepository;
import com.cosson.socialnetwork.response.FailureResult;
import com.cosson.socialnetwork.response.FriendListResult;
import com.cosson.socialnetwork.response.SimpleResult;
import com.cosson.socialnetwork.response.SubscriberResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FriendManagementService {

    private static final String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public Object createConnection(List<String> friends) {
        if (friends.size() != 2) {
            return new FailureResult(false, "Request body should include two user email address");
        }
        if (areFriends(friends.get(0), friends.get(1))) {
            return new FailureResult(false, "These two users are connected already");
        } else if (isTargetBlockedByRequestor(friends.get(1), friends.get(0))) {
            return new FailureResult(false, "user " + friends.get(1) + " is blocked by user " + friends.get(0));
        } else {
            //assume still can create connection even if the acceptor already block the requester
            Friends item = new Friends(friends.get(0), friends.get(1));
            friendRepository.save(item);
            return new SimpleResult(true);
        }
    }

    public Object retrieveFriendListResult(String userName) {
        List<String> friends = retrieveFriendList(userName);
        if (friends.size() > 0) {
            return new FriendListResult(true, friends);
        } else {
            return new FailureResult(false, "This user doesn't have any friend");
        }
    }

    public Object retrieveCommonFriendList(List<String> friends) {
        if (friends.size() != 2) {
            return new FailureResult(false, "Request body should include two user email address");
        }
        List<String> userAfriends = retrieveFriendList(friends.get(0));
        List<String> userBfriends = retrieveFriendList(friends.get(1));
        userAfriends.retainAll(userBfriends);
        if (userAfriends.size() > 0) {
            return new FriendListResult(true, userAfriends);
        } else {
            return new FailureResult(false, "These two users don't have any common friend");
        }
    }

    public Object subscribeUpdate(String requestor, String target) {
        if (isTargetBlockedByRequestor(target, requestor)) {
            return new FailureResult(false, "user " + requestor + " has been blocked by user " + target);
        } else if (isTargetSubscribedByRequestor(requestor, target)) {
            return new FailureResult(false, "user " + requestor + " has already subscribed update from user " + target);
        } else {
            Subscription subscription = new Subscription(requestor, target);
            subscriptionRepository.save(subscription);
            return new SimpleResult(true);
        }
    }

    public Object blockUpdates(String requestor, String target) {
        if (isTargetBlockedByRequestor(requestor, target)) {
            return new FailureResult(false, "user " + requestor + " has already blocked user " + target);
        } else {
            Block block = new Block(requestor, target);
            blockRepository.save(block);
            return new SimpleResult(true);
        }
    }

    public Object retrieveEligibleSubscriber(String sender, String text) {
        List<String> friendList = retrieveFriendList(sender);
        List<String> subscriberList = getSubscriberList(sender);
        List<String> mentionList = getMentionList(text);

        //list merging
        if (subscriberList != null & subscriberList.size() > 0) {
            friendList.removeAll(subscriberList);
            friendList.addAll(subscriberList);
        }

        //list merging
        if (mentionList != null & mentionList.size() > 0) {
            friendList.removeAll(mentionList);
            friendList.addAll(mentionList);
        }

        List<String> blockerList = getBlockerList(sender);
        if (blockerList != null & blockerList.size() > 0) {
            friendList.removeAll(blockerList);
        }

        if (friendList.size() > 0) {
            return new SubscriberResult(true, friendList);
        } else {
            return new FailureResult(false, "No one is eligible to receive the update from user " + sender);
        }
    }

    private boolean areFriends(String userA, String userB) {
        List<Friends> friendsList = friendRepository.findByRequestor(userA);
        for (Friends friends : friendsList) {
            if (friends.getAcceptor().equals(userB)) {
                return true;
            }
        }
        friendsList = friendRepository.findByAcceptor(userA);
        for (Friends friends : friendsList) {
            if (friends.getRequestor().equals(userB)) {
                return true;
            }
        }
        return false;
    }

    private List<String> retrieveFriendList(String userName) {
        List<String> friends = new ArrayList<>();
        List<Friends> friendsList = friendRepository.findByRequestor(userName);
        for (Friends friend : friendsList) {
            friends.add(friend.getAcceptor());
        }
        friendsList = friendRepository.findByAcceptor(userName);
        for (Friends friend : friendsList) {
            friends.add(friend.getRequestor());
        }
        return friends;
    }

    private boolean isTargetBlockedByRequestor(String requestor, String target) {
        Optional<Block> block = blockRepository.findByRequestorAndTarget(requestor, target);
        return block.isPresent();
    }

    private boolean isTargetSubscribedByRequestor(String requestor, String target) {
        Optional<Subscription> subscription = subscriptionRepository.findByRequestorAndTarget(requestor, target);
        return subscription.isPresent();
    }

    private List<String> getBlockerList(String target) {
        Optional<List<Block>> listOptional = blockRepository.findByTarget(target);
        if (listOptional.isPresent()) {
            List<Block> blockList = listOptional.get();
            return blockList.stream().map(block -> block.getRequestor()).collect(Collectors.toList());
        } else {
            return null;
        }
    }

    private List<String> getSubscriberList(String publisher) {
        Optional<List<Subscription>> listOptional = subscriptionRepository.findByTarget(publisher);
        if (listOptional.isPresent()) {
            List<Subscription> subscriberList = listOptional.get();
            return subscriberList.stream().map(subscriber -> subscriber.getRequestor()).collect(Collectors.toList());
        } else {
            return null;
        }
    }

    private List<String> getMentionList(String text) {
        Pattern p = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(text);
        List<String> mentionList = new ArrayList<>();
        while (matcher.find()) {
            mentionList.add(matcher.group());
        }
        return mentionList;
    }

    private List<String> mergeList(List<String> sourceList, List<String> targetList) {
        if (sourceList.size() == 0 && (targetList == null || targetList.size() == 0)) {
            return null;
        } else if (targetList == null || targetList.size() == 0) {
            return sourceList;
        } else
            sourceList.removeAll(targetList);
        sourceList.addAll(targetList);
        return sourceList;
    }
}
