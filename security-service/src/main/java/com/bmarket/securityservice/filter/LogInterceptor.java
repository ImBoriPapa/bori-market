package com.bmarket.securityservice.filter;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.bmarket.securityservice.utils.jwt.SecurityHeader.CLIENT_ID;


@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("인터셉터 시작인가?");
        String header = request.getHeader(CLIENT_ID);
        log.info("clientId={}",header);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("인터셉터 작동중인가?");
        String header = request.getHeader(CLIENT_ID);
        log.info("clientId={}",header);
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("인터셉터 끝인가?");
        String header = request.getHeader(CLIENT_ID);
        log.info("clientId={}",header);
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
