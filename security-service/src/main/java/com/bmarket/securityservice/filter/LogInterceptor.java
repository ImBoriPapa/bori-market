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
        String method = request.getMethod();
        String uri = request.getRequestURI();
        log.info("[LogInterceptor pre request method= {}, uri= {}]",method,uri);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        log.info("[LogInterceptor post request method= {}, uri= {}]",method,uri);
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        log.info("[LogInterceptor after request method= {}, uri= {}]",method,uri);
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
