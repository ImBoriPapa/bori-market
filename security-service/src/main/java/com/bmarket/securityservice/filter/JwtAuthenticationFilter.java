package com.bmarket.securityservice.filter;

import com.bmarket.securityservice.api.security.service.UserDetailServiceImpl;
import com.bmarket.securityservice.utils.jwt.JwtCode;

import com.bmarket.securityservice.api.security.service.JwtService;

import com.bmarket.securityservice.utils.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.bmarket.securityservice.utils.jwt.SecurityHeader.*;
import static com.bmarket.securityservice.utils.status.AuthenticationFilterStatus.*;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // TODO: 2022/11/14 메서드 설명 작성
    private final JwtService jwtService;
    private final JwtUtils jwtUtils;
    private final UserDetailServiceImpl userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> token = jwtUtils.resolveToken(request, AUTHORIZATION_HEADER);
        Optional<String> clientId = Optional.ofNullable(request.getHeader(CLIENT_ID));


        isEmptyClientId(request, clientId);

        isEmptyAccessToken(request, token);

        clientIdAndAccessTokenExist(request, response, token, clientId);

        filterChain.doFilter(request, response);
    }

    private void clientIdAndAccessTokenExist(HttpServletRequest request, HttpServletResponse response, Optional<String> token, Optional<String> clientId) {
        if (token.isPresent() && clientId.isPresent()) {
            String access = token.orElseThrow(() -> new IllegalArgumentException("토큰을 다시한번 확인해주세요"));
            JwtCode jwtCode = jwtUtils.validateToken(access);

            isAccessSuccessToken(access, jwtCode);
            isAccessExpiredToken(request, response, jwtCode);
            isAccessDENIED(request, jwtCode);

        }
    }

    private void isAccessDENIED(HttpServletRequest request, JwtCode jwtCode) {
        if (jwtCode == JwtCode.DENIED) {
            log.info("[잘못된 토큰입니다.]");
            request.setAttribute(FILTER_STATUS.name(), TOKEN_IS_DENIED);
        }
    }

    private void isAccessExpiredToken(HttpServletRequest request, HttpServletResponse response, JwtCode jwtCode) {
        if (jwtCode == JwtCode.EXPIRED) {
            log.info("[ACCESS TOKEN 이 만료되었습니다.]");
            jwtUtils.resolveToken(request, REFRESH_HEADER)
                    .ifPresentOrElse((t) -> refreshTokenValidation(request, response, t),
                            () -> request.setAttribute(FILTER_STATUS.name(), REFRESH_TOKEN_IS_EMPTY));
        }
    }

    private void isAccessSuccessToken(String token, JwtCode jwtCode) {
        if (jwtCode == JwtCode.ACCESS) {
            log.info("[ACCESS TOKEN 인증이 성공했습니다.]");
            setAuthentication(token);
        }
    }

    private void isEmptyAccessToken(HttpServletRequest request, Optional<String> token) {
        if (token.isEmpty()) {
            log.info("[ACCESS TOKEN 이 없습니다. 요청 URI= {}]", request.getRequestURI());
            request.setAttribute(FILTER_STATUS.name(), TOKEN_IS_EMPTY);
        }
    }

    private void isEmptyClientId(HttpServletRequest request, Optional<String> clientId) {
        if (clientId.isEmpty()) {
            log.info("[CLIENT-ID가 없습니다. 요청 URI= {}]", request.getRequestURI());
            request.setAttribute(FILTER_STATUS.name(), CLIENT_ID_EMPTY);
        }
    }

    public void refreshTokenValidation(HttpServletRequest request, HttpServletResponse response, String token) {
        JwtCode jwtCode = jwtUtils.validateToken(token);
        if (jwtCode == JwtCode.ACCESS) {
            Long accountId = jwtUtils.getUserId(token);
            log.info("리프레쉬 토큰 인증이 성공하였습니다.");
            Authentication authentication = userDetailService.generateAuthentication(accountId);
            String generateToken = jwtUtils.generateAccessToken(accountId);
            log.info("ACCESS 토큰 재발급이 성공하였습니다.");
            String reissueRefreshToken = jwtService.reissueRefreshToken(token, accountId);
            log.info("REFRESH 토큰 재발급이 성공하였습니다.");
            String clientId = jwtService.reGeneratedClientId(accountId);
            log.info("Client Id 를 갱신 하였습니다.");

            response.setHeader(CLIENT_ID, clientId);
            response.setHeader(AUTHORIZATION_HEADER, JWT_HEADER_PREFIX + generateToken);
            response.setHeader(REFRESH_HEADER, JWT_HEADER_PREFIX + reissueRefreshToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        if (jwtCode == JwtCode.EXPIRED) {
            log.info("리프레쉬 토큰이 만료되었습니다.");
            request.setAttribute(FILTER_STATUS.name(), REFRESH_TOKEN_IS_EXPIRED);
        }

        if (jwtCode == JwtCode.DENIED) {
            log.info("리프레쉬 토큰이 잘못되었습니다.");
            request.setAttribute(FILTER_STATUS.name(), REFRESH_TOKEN_IS_DENIED);
        }
    }

    public void setAuthentication(String jwt) {
        log.info("[인증정보 저장]");
        Authentication authentication = userDetailService.generateAuthentication(jwtUtils.getUserId(jwt));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
