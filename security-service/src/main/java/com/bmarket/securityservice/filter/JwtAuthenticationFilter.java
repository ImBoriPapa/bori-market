package com.bmarket.securityservice.filter;

import com.bmarket.securityservice.domain.security.service.UserDetailServiceImpl;
import com.bmarket.securityservice.utils.jwt.JwtCode;

import com.bmarket.securityservice.domain.security.service.JwtService;

import com.bmarket.securityservice.utils.jwt.JwtUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.bmarket.securityservice.utils.jwt.SecurityHeader.*;
import static com.bmarket.securityservice.utils.status.AuthenticationFilterStatus.*;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final JwtUtils jwtUtils;
    private final UserDetailServiceImpl userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("[JwtAuthenticationFilter]");

        String clientId = clientIdCheck(request);

        tokenAndIdHandle(request, response, clientId);

        filterChain.doFilter(request, response);
    }

    /**
     * 토큰과 클라이언트 아이디가 존재할 경우 처리
     */
    private void tokenAndIdHandle(HttpServletRequest request, HttpServletResponse response, String clientId) {
        jwtUtils.resolveToken(request, AUTHORIZATION_HEADER)
                .ifPresentOrElse(
                        (t) -> clientIdAndAccessTokenExist(request, response, t, clientId),
                        () -> isEmptyAccessToken(request)
                );
    }

    /**
     * 클라이언트 아이디가 존재하는지 확인
     */
    private String clientIdCheck(HttpServletRequest request) {
        String clientId = request.getHeader(CLIENT_ID);
        isClientIdExist(request, clientId);
        return clientId;
    }

    /**
     * 클라이언트 아이디가 존재하지 않을 경우 처리
     */
    private void isClientIdExist(HttpServletRequest request, String clientId) {
        if (clientId == null)
            request.setAttribute(CLIENT_ID_STATUS.name(), CLIENT_ID_EMPTY);
    }

    /**
     * 클라이언트 아이디와 토큰을 검사 후 처리
     */
    private void clientIdAndAccessTokenExist(HttpServletRequest request, HttpServletResponse response, String token, String clientId) {
        if (StringUtils.hasLength(token) && StringUtils.hasLength(clientId)) {

            JwtCode jwtCode = jwtUtils.validateToken(token);

            log.info("[CLIENT ID 검사]");
            if (jwtService.clientIdCheck(clientId)) {
                isAccessSuccessToken(token, jwtCode);
                isAccessExpiredToken(request, response, jwtCode);
                isAccessDENIED(request, jwtCode);
            } else {
                request.setAttribute(CLIENT_ID_STATUS.name(), CLIENT_ID_IS_INVALID);
            }

        }
    }

    /**
     * 잘못된 AccessToken 일 경우 처리
     */
    private void isAccessDENIED(HttpServletRequest request, JwtCode jwtCode) {
        if (jwtCode == JwtCode.DENIED) {
            log.info("[잘못된 토큰입니다.]");
            request.setAttribute(FILTER_STATUS.name(), TOKEN_IS_DENIED);
        }
    }

    /**
     * 만료된 AccessToken 일 경우 Refresh Token 을 확인 후 처리
     */
    private void isAccessExpiredToken(HttpServletRequest request, HttpServletResponse response, JwtCode jwtCode) {
        if (jwtCode == JwtCode.EXPIRED) {
            log.info("[ACCESS TOKEN 이 만료되었습니다.]");
            jwtUtils.resolveToken(request, REFRESH_HEADER)
                    .ifPresentOrElse((t) -> refreshTokenValidation(request, response, t),
                            () -> request.setAttribute(FILTER_STATUS.name(), REFRESH_TOKEN_IS_EMPTY));
        }
    }

    /**
     * AccessToken 검증에 성공했을 경우
     */
    private void isAccessSuccessToken(String token, JwtCode jwtCode) {
        if (jwtCode == JwtCode.ACCESS) {
            log.info("[ACCESS TOKEN 인증이 성공했습니다.]");
            setAuthentication(token);
        }
    }

    /**
     * AccessToken 이 없을 경우
     */
    private void isEmptyAccessToken(HttpServletRequest request) {
        log.info("[ACCESS TOKEN 이 없습니다. 요청 URI= {}]", request.getRequestURI());
        request.setAttribute(FILTER_STATUS.name(), TOKEN_IS_EMPTY);
    }

    /**
     * RefreshToken 검증 후 처리
     */
    private void refreshTokenValidation(HttpServletRequest request, HttpServletResponse response, String token) {
        JwtCode jwtCode = jwtUtils.validateToken(token);
        refreshIsAccess(response, token, jwtCode);

        refreshIsExpired(request, jwtCode);

        refreshIsDenied(request, jwtCode);
    }

    /**
     * RefreshToken 이 잘못된 경우 처리
     */
    private void refreshIsDenied(HttpServletRequest request, JwtCode jwtCode) {
        if (jwtCode == JwtCode.DENIED) {
            log.info("리프레쉬 토큰이 잘못되었습니다.");
            request.setAttribute(FILTER_STATUS.name(), REFRESH_TOKEN_IS_DENIED);
        }
    }

    /**
     * RefreshToken 이 만료된 경우 처리
     */
    private void refreshIsExpired(HttpServletRequest request, JwtCode jwtCode) {
        if (jwtCode == JwtCode.EXPIRED) {
            log.info("리프레쉬 토큰이 만료되었습니다.");
            request.setAttribute(FILTER_STATUS.name(), REFRESH_TOKEN_IS_EXPIRED);
        }
    }

    /**
     * RefreshToken 이 정상인 경우
     * Access 토큰,Refresh 토큰,clientId 재발급
     */
    private void refreshIsAccess(HttpServletResponse response, String token, JwtCode jwtCode) {
        if (jwtCode == JwtCode.ACCESS) {
            Long accountId = jwtUtils.getUserId(token);
            log.info("리프레쉬 토큰 인증이 성공하였습니다.");
            Authentication authentication = userDetailService.generateAuthentication(accountId);
            String generateToken = jwtUtils.generateAccessToken(accountId);
            log.info("ACCESS 토큰 재발급이 성공하였습니다.");
            String reissueRefreshToken = jwtService.reissueRefreshToken(token, accountId);
            log.info("REFRESH 토큰 재발급이 성공하였습니다.");
            String clientId = jwtService.reGenerateClientId(accountId);
            log.info("Client Id 를 갱신 하였습니다.");

            response.setHeader(CLIENT_ID, clientId);
            response.setHeader(AUTHORIZATION_HEADER, JWT_HEADER_PREFIX + generateToken);
            response.setHeader(REFRESH_HEADER, JWT_HEADER_PREFIX + reissueRefreshToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    /**
     * 인증 객체 저장
     */
    private void setAuthentication(String jwt) {
        log.info("[인증정보 저장]");
        Authentication authentication = userDetailService.generateAuthentication(jwtUtils.getUserId(jwt));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
