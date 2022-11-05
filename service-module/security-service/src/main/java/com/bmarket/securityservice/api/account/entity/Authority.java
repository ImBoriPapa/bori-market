package com.bmarket.securityservice.api.account.entity;

import lombok.Getter;

@Getter
public enum Authority {

    ROLL_USER("ROLL_USER"),
    ROLL_ADMIN("ROLL_USER,ROLL_ADMIN"),
    ROLL_SUPER_ADMIN("ROLL_USER,ROLL_ADMIN,ROLL_SUPER_ADMIN");

    public final String ROLL;

    Authority(String ROLL) {
        this.ROLL = ROLL;
    }

}
