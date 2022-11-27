package com.bmarket.securityservice.utils.regex;

import lombok.Getter;

@Getter
public class Regex {
    public static final String ENG_NUMBER = "^[a-zA-Z0-9]*$";
    public static final String KOR_ENG = "^[ㄱ-ㅎ|가-힣|a-zA-Z]*$";
    public static final String NUMBER = "^[0-9]*$";
    public static final String KOR_ENG_NUMBER = "^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|]+$";

    public static final String PASSWORD = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$";
}
