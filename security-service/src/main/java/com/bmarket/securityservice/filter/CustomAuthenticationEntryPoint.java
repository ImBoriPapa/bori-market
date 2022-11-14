package com.bmarket.securityservice.filter;


import com.bmarket.securityservice.utils.status.AuthenticationFilterStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.bmarket.securityservice.utils.status.AuthenticationFilterStatus.*;


/**
 * 인증실패 핸들링
 * AuthenticationFailResponseController 에 응답 역할을 위임
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("[CustomAuthenticationEntryPoint 동작]");
        AuthenticationFilterStatus tokenStatus = (AuthenticationFilterStatus) request.getAttribute(FILTER_STATUS.name());
        AuthenticationFilterStatus clientIdStatus = (AuthenticationFilterStatus) request.getAttribute(CLIENT_ID_STATUS.name());

        log.info("[request tokenStatus] ={}", tokenStatus);
        log.info("[request clientIdStatus] ={}", clientIdStatus);

        bothStatusEmpty(response, tokenStatus, clientIdStatus);

        isClientIdEmpty(response, tokenStatus, clientIdStatus);

        clientIdIsInvalid(response, clientIdStatus);

        tokenHandle(response, tokenStatus, clientIdStatus);
    }

    /**
     * Client Id 에 문제가 없을시 token 문제 핸들링
     */
    private void tokenHandle(HttpServletResponse response, AuthenticationFilterStatus tokenStatus, AuthenticationFilterStatus clientIdStatus) throws IOException {
        if (clientIdStatus == null) {
            isAccessEmpty(response, tokenStatus);

            isAccessInvalid(response, tokenStatus);

            isRefreshEmpty(response, tokenStatus);

            isRefreshExpired(response, tokenStatus);

            isRefreshInvalid(response, tokenStatus);
        }
    }

    /**
     * Refresh token  = 잘못된 리프레시 토큰
     */
    private void isRefreshInvalid(HttpServletResponse response, AuthenticationFilterStatus tokenStatus) throws IOException {
        if (tokenStatus == REFRESH_TOKEN_IS_DENIED) {
            log.info("REFRESH_TOKEN_IS_DENIED Handling");
            response.sendRedirect(REFRESH_TOKEN_IS_DENIED.getUrl());
        }
    }

    /**
     * Refresh token  = 유효기간 만료
     */
    private void isRefreshExpired(HttpServletResponse response, AuthenticationFilterStatus tokenStatus) throws IOException {
        if (tokenStatus == REFRESH_TOKEN_IS_EXPIRED) {
            log.info("REFRESH_TOKEN_IS_EXPIRED Handling");
            response.sendRedirect(REFRESH_TOKEN_IS_EXPIRED.getUrl());
        }
    }

    /**
     * Refresh token  = empty
     */
    private void isRefreshEmpty(HttpServletResponse response, AuthenticationFilterStatus tokenStatus) throws IOException {
        if (tokenStatus == REFRESH_TOKEN_IS_EMPTY) {
            log.info("REFRESH_TOKEN_IS_EMPTY Handling");
            response.sendRedirect(REFRESH_TOKEN_IS_EMPTY.getUrl());
        }
    }

    /**
     * Access Token  = 잘못된 Access token
     */
    private void isAccessInvalid(HttpServletResponse response, AuthenticationFilterStatus tokenStatus) throws IOException {
        if (tokenStatus == TOKEN_IS_DENIED) {
            log.info("TOKEN_IS_DENIED Handling");
            response.sendRedirect(TOKEN_IS_DENIED.getUrl());
        }
    }

    /**
     * TOKEN_IS_EMPTY  = empty
     */
    private void isAccessEmpty(HttpServletResponse response, AuthenticationFilterStatus tokenStatus) throws IOException {
        if (tokenStatus == TOKEN_IS_EMPTY) {
            log.info("TOKEN_IS_EMPTY Handling");
            response.sendRedirect(TOKEN_IS_EMPTY.getUrl());
        }
    }

    /**
     * clientIdIsInvalid  = 잘못된 클라이언트 아이디
     */
    private void clientIdIsInvalid(HttpServletResponse response, AuthenticationFilterStatus clientIdStatus) throws IOException {
        if (clientIdStatus == CLIENT_ID_IS_INVALID) {
            log.info("CLIENT_ID_IS_WRONG Handling");
            response.sendRedirect(CLIENT_ID_IS_INVALID.getUrl());
        }
    }

    /**
     * isClientIdEmpty  = empty
     */
    private void isClientIdEmpty(HttpServletResponse response, AuthenticationFilterStatus tokenStatus, AuthenticationFilterStatus clientIdStatus) throws IOException {
        if (clientIdStatus == CLIENT_ID_EMPTY && tokenStatus != TOKEN_IS_EMPTY) {
            log.info("CLIENT_ID_IS_EMPTY Handling");
            response.sendRedirect(CLIENT_ID_EMPTY.getUrl());
        }
    }

    /**
     * clientIdStatus,tokenStatus  = empty
     */
    private void bothStatusEmpty(HttpServletResponse response, AuthenticationFilterStatus tokenStatus, AuthenticationFilterStatus clientIdStatus) throws IOException {
        if (clientIdStatus == CLIENT_ID_EMPTY && tokenStatus == TOKEN_IS_EMPTY) {
            log.info("CLIENT_ID_IS_EMPTY Handling");
            response.sendRedirect(EMPTY_BOTH.url);
        }
    }


}
