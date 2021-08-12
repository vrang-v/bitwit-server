package com.server.bitwit.module.security.jwt.support;

import com.server.bitwit.module.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    
    private final JwtService jwtService;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
    throws IOException, ServletException {
        var httpRequest = (HttpServletRequest)request;
        
        var jwt = jwtService.extractJwt(httpRequest);
        
        if (jwtService.isValidJwt(jwt)) {
            var authentication = jwtService.extractAuthentication(jwt);
            SecurityContextHolder.getContext( ).setAuthentication(authentication);
        }
        
        filterChain.doFilter(request, response);
    }
}
