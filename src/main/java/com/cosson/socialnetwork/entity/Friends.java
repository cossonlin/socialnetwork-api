package com.cosson.socialnetwork.entity;

import javax.persistence.*;

@Entity(name = "FRIENDS")
public class Friends {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    @Column(name = "REQUESTOR")
    private String requestor;
    @Column(name = "ACCEPTOR")
    private String acceptor;

    public Friends(){}

    public Friends(String requestor, String acceptor) {
        this.requestor = requestor;
        this.acceptor = acceptor;
    }

    public String getRequestor() {
        return requestor;
    }

    public String getAcceptor() {
        return acceptor;
    }
}
