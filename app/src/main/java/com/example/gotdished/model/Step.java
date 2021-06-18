package com.example.gotdished.model;

import java.io.Serializable;

public class Step implements Serializable {
    private String details;

    public Step(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Step () {}
}
