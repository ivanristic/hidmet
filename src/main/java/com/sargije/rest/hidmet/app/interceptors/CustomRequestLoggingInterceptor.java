package com.sargije.rest.hidmet.app.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomRequestLoggingInterceptor extends HandlerInterceptorAdapter {
    Logger logger = LoggerFactory.getLogger(CustomRequestLoggingInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        logger.info("preHandle => Request URL: {}", request.getRequestURL());
        logger.info("preHandle => Remote Address: {}", request.getRemoteAddr());
       // logger.info("preHandle => Path: {}", request.getPathInfo());
       // logger.info("preHandle => Query: {}", request.getQueryString());
       // logger.info("preHandle => User: {}", request.getUserPrincipal().getName());
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        logger.info("afterCompletion => Response Status: {}", response.getStatus());
    }

}
