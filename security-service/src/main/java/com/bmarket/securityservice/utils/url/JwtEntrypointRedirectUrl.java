package com.bmarket.securityservice.utils.url;

public class JwtEntrypointRedirectUrl {
    public final static String EMPTY_CLIENT_ID = "/empty-clientId";
    public final static String EMPTY_TOKEN = "/empty-token";
    public final static String EXPIRED_TOKEN = "/expired-token";
    public final static String DENIED_TOKEN = "/denied-token";

    public final static String REDIRECT_EXCEPTION_EMPTY_CLIENT_ID = "/exception/empty-clientId";
    public final static String REDIRECT_EXCEPTION_EMPTY_ACCESS_TOKEN = "/exception/empty-token?token=access";
    public final static String REDIRECT_EXCEPTION_EMPTY_REFRESH_TOKEN = "/exception/empty-token?token=refresh";
    public final static String REDIRECT_EXCEPTION_EXPIRED_REFRESH_TOKEN = "/exception/expired-token";
    public final static String REDIRECT_EXCEPTION_DENIED_ACCESS_TOKEN = "/exception/denied-token?token=access";
    public final static String REDIRECT_EXCEPTION_DENIED_REFRESH_TOKEN = "/exception/denied-token?token=refresh";


}
