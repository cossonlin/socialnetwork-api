package com.cosson.socialnetwork.request;

public class RetrieveFriendList {
    private String email;

    public RetrieveFriendList() {
    }

    public RetrieveFriendList(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
