package com.bmarket.securityservice.event;

import lombok.Getter;

@Getter
public class Event {

    private String name;

    public Event(String name) {
        this.name = name;
    }
}
