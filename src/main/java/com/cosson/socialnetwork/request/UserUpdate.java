package com.cosson.socialnetwork.request;

public class UserUpdate {
    private String sender;
    private String text;

    public UserUpdate() {
    }

    public UserUpdate(String sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }
}
