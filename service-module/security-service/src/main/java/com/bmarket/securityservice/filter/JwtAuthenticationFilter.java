package com.bmarket.securityservice.filter;

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

import static com.bmarket.securityservice.utils.jwt.JwtHeader.*;
import static com.bmarket.securityservice.utils.status.JwtTokenStatus.*;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> token = jwtUtils.resolveToken(request, AUTHORIZATION_HEADER);
        JwtCode jwtCode;
        if (token.isEmpty()) {
            log.info("ACCESS TOKEN 이 없습니다.", request.getRequestURI());
            request.setAttribute(JWT_TOKEN_STATUS.name(), TOKEN_IS_EMPTY);
        }

        if (token.isPresent()) {
            jwtCode = jwtUtils.validateToken(token.get());

            switch (jwtCode) {
                case ACCESS:
                    setAuthentication(token.get());
                    log.info("토큰 인증이 성공했습니다.");
                    break;
                case EXPIRED:
                    token = jwtUtils.resolveToken(request, REFRESH_HEADER);

                    log.info("ACCESS TOKEN 이 만료 되었습니다.");
                    token.ifPresentOrElse((m) -> refreshTokenValidation(request, response, m),
                            () -> request.setAttribute(JWT_TOKEN_STATUS.name(), REFRESH_TOKEN_IS_EMPTY)
                    );
                    break;
                case DENIED:
                    request.setAttribute(JWT_TOKEN_STATUS.name(), TOKEN_IS_DENIED);
                    log.info("잘못된 토큰입니다.");
                    break;
            }
        }
        filterChain.doFilter(request, response);
    }

    public void refreshTokenValidation(HttpServletRequest request, HttpServletResponse response, String token) {
        JwtCode jwtCode = jwtUtils.validateToken(token);
        if (jwtCode == JwtCode.ACCESS) {
            log.info("리프레쉬 토큰 인증이 성공하였습니다.");
            Authentication authentication = jwtService.getAuthentication(token);
            String generateToken = jwtUtils.generateToken(authentication.getName());
            log.info("ACCESS 토큰 재발급이 성공하였습니다.");
            String reissueRefreshToken = jwtService.reissueRefreshToken(token);
            log.info("REFRESH 토큰 재발급이 성공하였습니다.");
            response.setHeader(AUTHORIZATION_HEADER, JWT_HEADER_PREFIX + generateToken);
            response.setHeader(REFRESH_HEADER, JWT_HEADER_PREFIX + reissueRefreshToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        if (jwtCode == JwtCode.EXPIRED) {
            log.info("리프레쉬 토큰이 만료되었습니다.");
            request.setAttribute(JWT_TOKEN_STATUS.name(), REFRESH_TOKEN_IS_EXPIRED);
        }

        if (jwtCode == JwtCode.DENIED) {
            log.info("리프레쉬 토큰이 잘못되었습니다.");
            request.setAttribute(JWT_TOKEN_STATUS.name(), REFRESH_TOKEN_IS_DENIED);
        }
    }

    public void setAuthentication(String jwt) {
        Authentication authentication = jwtService.getAuthentication(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
