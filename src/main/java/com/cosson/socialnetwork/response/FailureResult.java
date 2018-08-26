package com.cosson.socialnetwork.response;

public class FailureResult extends SimpleResult {

    private String message;

    public FailureResult(boolean success, String message) {
        super(success);
        this.message = message;
    }
}
