package com.bmarket.securityservice.filter;

import com.bmarket.securityservice.domain.security.service.JwtService;
import com.bmarket.securityservice.domain.security.service.UserDetailServiceImpl;
import com.bmarket.securityservice.exception.custom_exception.security_ex.InvalidTokenException;
import com.bmarket.securityservice.utils.jwt.JwtCode;
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

        accessTokenHandle(request, response);

        filterChain.doFilter(request, response);
    }

    /**
     * access 토큰 처리
     * access 토큰이 있다면 accessToken()
     * access 토큰이 없다면 isEmptyAccessToken()
     */
    private void accessTokenHandle(HttpServletRequest request, HttpServletResponse response) {
        jwtUtils.resolveToken(request, AUTHORIZATION_HEADER)
                .ifPresentOrElse(
                        (t) -> accessTokenExist(request, response, t),
                        () -> isEmptyAccessToken(request)
                );
    }

    /**
     * access 토큰을 검사
     */
    private void accessTokenExist(HttpServletRequest request, HttpServletResponse response, String token) {
        if (StringUtils.hasLength(token)) {
            log.info("[AccessToken= {}]", token);
            JwtCode jwtCode = jwtUtils.validateToken(token);

            isAccessSuccessToken(token, jwtCode);
            isAccessExpiredToken(request, response, jwtCode);
            isAccessDENIED(request, jwtCode);

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
        log.info("[ACCESS TOKEN이 없습니다.]");
        request.setAttribute(FILTER_STATUS.name(), TOKEN_IS_EMPTY);
    }

    /**
     * RefreshToken 검증 후 처리
     */
    private void refreshTokenValidation(HttpServletRequest request, HttpServletResponse response, String token) {
        JwtCode jwtCode = jwtUtils.validateToken(token);
        refreshIsAccess(response, request, token, jwtCode);

        refreshIsExpired(request, jwtCode);

        refreshIsDenied(request, jwtCode);
    }

    /**
     * RefreshToken 이 잘못된 경우 처리
     */
    private void refreshIsDenied(HttpServletRequest request, JwtCode jwtCode) {
        if (jwtCode == JwtCode.DENIED) {
            log.info("리프레쉬 토큰이 잘못되었습니다.");
            SecurityContextHolder.clearContext();
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
     * Access 토큰,Refresh 토큰
     */
    private void refreshIsAccess(HttpServletResponse response, HttpServletRequest request, String token, JwtCode jwtCode) {
        if (jwtCode == JwtCode.ACCESS) {
            log.info("리프레쉬 토큰 인증상태는 ACCESS 입니다.");
            Long accountId = jwtUtils.getUserId(token);

            String generateToken = jwtUtils.generateAccessToken(accountId);
            log.info("ACCESS 토큰 재발급이 성공하였습니다.");

            if (refreshTokenIsMatchedWithStored(response, token, accountId, generateToken)) {
                setAuthentication(generateToken);
            } else {
                request.setAttribute(FILTER_STATUS.name(), REFRESH_TOKEN_IS_DENIED);
            }
        }
    }

    /**
     * refresh token 을 데이터베이스에 저장값과 확인후 일치하지 않으면 InvalidTokenException
     */
    private boolean refreshTokenIsMatchedWithStored(HttpServletResponse response, String token, Long accountId, String generateToken) {
        try {
            String reissueRefreshToken = jwtService.reissueRefreshToken(token, accountId);
            log.info("REFRESH 토큰 재발급이 성공하였습니다.");
            response.setHeader(AUTHORIZATION_HEADER, JWT_HEADER_PREFIX + generateToken);
            response.setHeader(REFRESH_HEADER, JWT_HEADER_PREFIX + reissueRefreshToken);
            return true;
        } catch (InvalidTokenException e) {
            log.info("잘못된 리프레시인증 토큰입니다.");
            // TODO: 2022/12/08 SecurityContextHolder.clearContext() 를 해줘야 할지 공부해보자.
            SecurityContextHolder.clearContext();
            return false;
        }

    }

    /**
     * 인증 객체 저장
     */
    private void setAuthentication(String jwt) {
        Authentication authentication = userDetailService.generateAuthentication(jwtUtils.getUserId(jwt));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("[인증정보 저장]");
        log.info("SecurityContextHolder.getContext().getAuthentication()= {}", SecurityContextHolder.getContext().getAuthentication());
    }


}
