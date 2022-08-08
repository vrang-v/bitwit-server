package com.server.bitwit.module.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) {
        var requestUri = getRequestUri(request);
        log.info("[REQUEST]  [{}] [{}]", requestUri, request.getRemoteAddr( ));
        return true;
    }
    
    @Override
    public void afterCompletion(@Nonnull HttpServletRequest request, HttpServletResponse response, @Nonnull Object handler, Exception ex) {
        var requestUri = getRequestUri(request);
        log.info("[RESPONSE] [{}] [{}]", requestUri, response.getStatus( ));
    }
    
    private static String getRequestUri(HttpServletRequest request) {
        var requestUri = request.getRequestURI( );
        if (request.getQueryString( ) != null) {
            requestUri += "?" + request.getQueryString( );
        }
        return requestUri;
    }
}
