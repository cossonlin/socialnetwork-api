package com.cosson.socialnetwork.response;

import java.util.List;

public class SubscriberResult extends SimpleResult {
    private List<String> recipients;

    public SubscriberResult() {
    }

    public SubscriberResult(boolean success, List<String> recipients) {
        super(success);
        this.recipients = recipients;
    }

    public List<String> getRecipients() {
        return recipients;
    }
}
