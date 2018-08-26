package com.cosson.socialnetwork.entity;

import javax.persistence.*;

@Entity(name = "SUBSCRIPTION")
public class Subscription {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    @Column(name = "REQUESTOR")
    private String requestor;
    @Column(name = "TARGET")
    private String target;

    public Subscription() {
    }

    public Subscription(String requestor, String target) {
        this.requestor = requestor;
        this.target = target;
    }

    public String getRequestor() {
        return requestor;
    }
}
