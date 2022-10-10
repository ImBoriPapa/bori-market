package com.bmarket.securityservice.filter;

import com.bmarket.securityservice.entity.JwtCode;
import com.bmarket.securityservice.service.JwtService;
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

import static com.bmarket.securityservice.entity.JwtHeader.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveToken(request, AUTHORIZATION_HEADER);

        if (!StringUtils.hasLength(jwt)) {
            log.info("No valid JWT token found ={}", request.getRequestURI());

        }

        if (jwtService.validateToken(jwt) == JwtCode.ACCESS) {
            setJwtTokenToContext(jwt);
            filterChain.doFilter(request, response);
            return;
        }

        if (jwtService.validateToken(jwt) == JwtCode.EXPIRED) {
            String refresh = resolveToken(request, REFRESH_HEADER);

            if (!StringUtils.hasLength(refresh)) {
                log.info("refresh is null");
                throw new IllegalArgumentException("NO refresh TOKEN");
            }

            if (jwtService.validateToken(refresh) == JwtCode.ACCESS) {
                String newRefresh = jwtService.reissueRefreshToken(refresh);
                Authentication authentication = jwtService.getAuthentication(refresh);
                String token = jwtService.generateToken(authentication);
                response.setHeader(AUTHORIZATION_HEADER, token);
                response.setHeader(REFRESH_HEADER, newRefresh);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    public void setJwtTokenToContext(String jwt) {
        Authentication authentication = jwtService.getAuthentication(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public String resolveToken(HttpServletRequest request, String header) {
        String bearerToken = request.getHeader(header);
        if (StringUtils.hasLength(bearerToken) && bearerToken.startsWith(JWT_HEADER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
