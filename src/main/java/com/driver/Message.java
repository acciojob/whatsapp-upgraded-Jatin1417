package com.driver;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.Date;


public class Message {
    private int id;
    private String content;
    private Date timestamp;

    public Message(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Message() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
