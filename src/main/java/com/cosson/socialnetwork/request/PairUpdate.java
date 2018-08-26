package com.cosson.socialnetwork.request;

import org.springframework.stereotype.Component;

@Component
public class PairUpdate {
    private String requestor;
    private String target;

    public PairUpdate() {
    }

    public PairUpdate(String requestor, String target) {
        this.requestor = requestor;
        this.target = target;
    }

    public String getRequestor() {
        return requestor;
    }

    public String getTarget() {
        return target;
    }
}
