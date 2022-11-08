package com.bmarket.securityservice.filter;


import com.bmarket.securityservice.utils.status.JwtTokenStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.bmarket.securityservice.utils.url.JwtEntrypointRedirectUrl.*;


/**
 * 인증되지 않는 접근 처리
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("CustomAuthenticationEntryPoint 동작");
        JwtTokenStatus status = (JwtTokenStatus) request.getAttribute(JwtTokenStatus.JWT_TOKEN_STATUS.name());

        switch (status) {
            case TOKEN_IS_EMPTY:
                log.info("TOKEN_IS_EMPTY REDIRECT->/exception/empty-token");
                response.sendRedirect(REDIRECT_EXCEPTION_EMPTY_ACCESS_TOKEN);
                break;
            case TOKEN_IS_DENIED:
                log.info("TOKEN_IS_DENIED REDIRECT->/exception/denied-token");
                response.sendRedirect(REDIRECT_EXCEPTION_DENIED_ACCESS_TOKEN);
                break;
            case REFRESH_TOKEN_IS_EMPTY:
                log.info("REFRESH_TOKEN_IS_EMPTY REDIRECT->/exception/empty?token=refresh");
                response.sendRedirect(REDIRECT_EXCEPTION_EMPTY_REFRESH_TOKEN);
                break;
            case REFRESH_TOKEN_IS_EXPIRED:
                log.info("REFRESH_TOKEN_IS_EXPIRED REDIRECT->/exception/expired");
                response.sendRedirect(REDIRECT_EXCEPTION_EXPIRED_REFRESH_TOKEN);
                break;
            case REFRESH_TOKEN_IS_DENIED:
                log.info("REFRESH_TOKEN_IS_DENIED REDIRECT->/exception/denied-token");
                response.sendRedirect(REDIRECT_EXCEPTION_DENIED_REFRESH_TOKEN);
                break;
        }

    }


}
