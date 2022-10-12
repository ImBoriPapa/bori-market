package com.bmarket.securityservice.filter;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.error_code.ErrorCode;
import com.bmarket.securityservice.utils.status.JwtTokenStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;





/**
 * 인증되지 않는 접근 처리
 */
@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("CustomAuthenticationEntryPoint 동작");
        JwtTokenStatus status =(JwtTokenStatus) request.getAttribute(JwtTokenStatus.JWT_TOKEN_STATUS.name());


        switch (status) {
            case TOKEN_IS_EMPTY:
                log.info("TOKEN_IS_EMPTY");
                response.setStatus(HttpStatus.FORBIDDEN.value());
                break;
            case TOKEN_IS_DENIED:
                log.info("TOKEN_IS_DENIED");
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                break;
            case REFRESH_TOKEN_IS_EMPTY:
                log.info("REFRESH_TOKEN_IS_EMPTY");
                response.setStatus(HttpStatus.FORBIDDEN.value());
                break;
            case REFRESH_TOKEN_IS_EXPIRED:
                log.info("REFRESH_TOKEN_IS_EXPIRED");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

    }


}
