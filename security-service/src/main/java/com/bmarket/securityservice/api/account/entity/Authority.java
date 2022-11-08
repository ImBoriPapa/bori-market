package com.bmarket.securityservice.api.account.entity;

import lombok.Getter;

@Getter
public enum Authority {

    USER("ROLL_USER"),
    ADMIN("ROLL_USER,ROLL_ADMIN"),
    SUPER_ADMIN("ROLL_USER,ROLL_ADMIN,ROLL_SUPER_ADMIN");

    public final String ROLL;

    Authority(String ROLL) {
        this.ROLL = ROLL;
    }

}
