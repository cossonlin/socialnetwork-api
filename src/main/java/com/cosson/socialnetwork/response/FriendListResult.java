package com.cosson.socialnetwork.response;

import java.util.List;

public class FriendListResult extends SimpleResult {
    private List<String> friends;
    private long count;

    public FriendListResult() {
    }

    public FriendListResult(boolean success, List<String> friends) {
        super(success);
        this.friends = friends;
        this.count = friends.size();
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
