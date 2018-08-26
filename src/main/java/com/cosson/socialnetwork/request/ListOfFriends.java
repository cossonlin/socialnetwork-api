package com.cosson.socialnetwork.request;

import java.util.List;

public class ListOfFriends {
    private List<String> friends;

    public ListOfFriends() {
    }

    public ListOfFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getFriends() {
        return friends;
    }

    @Override
    public String toString() {
        return "{" +
                "friends:[" +
                friends == null ? "" : String.join(",", friends) +
                "]}";
    }
}
