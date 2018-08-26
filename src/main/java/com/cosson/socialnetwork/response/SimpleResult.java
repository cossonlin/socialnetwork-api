package com.cosson.socialnetwork.response;

public class SimpleResult {
    private boolean success;

    public SimpleResult() {
    }

    public SimpleResult(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
