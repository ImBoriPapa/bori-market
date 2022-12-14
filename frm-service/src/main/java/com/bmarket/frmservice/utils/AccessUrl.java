package com.bmarket.frmservice.utils;


import org.springframework.beans.factory.annotation.Value;

/**
 * 이미지 접근 경로
 */
public class AccessUrl {
    public static String PROFILE_URL = "http://localhost:8095/file/profile-images/";
    public static String TRADE_URL = "http://localhost:8095/file/trade-images/";
    public static String DEFAULT_PROFILE_URL = "http://localhost:8095/file/default/";
    public static String DEFAULT_IMAGE_NAME = "dgiefault-profile.jpg";

    @Value("${resource-access.profile}")
    public static void setProfileUrl(String profileUrl) {
        PROFILE_URL = profileUrl;
    }

    @Value("${resource-access.trade}")
    public static void setTradeUrl(String tradeUrl) {
        TRADE_URL = tradeUrl;
    }

    @Value("${resource-access.default-profile}")
    public static void setDefaultProfileUrl(String defaultProfileUrl) {
        DEFAULT_PROFILE_URL = defaultProfileUrl;
    }
}
