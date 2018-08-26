package com.cosson.socialnetwork.entity;

import javax.persistence.*;

@Entity(name = "BLOCK")
public class Block {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    @Column(name = "REQUESTOR")
    private String requestor;
    @Column(name = "TARGET")
    private String target;

    public Block() {
    }

    public Block(String requestor, String target) {
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
