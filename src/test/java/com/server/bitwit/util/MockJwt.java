package com.server.bitwit.util;

import com.server.bitwit.module.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MockJwt {
    
    private final JwtService jwtService;
    
    public String getJwt( ) {
        return jwtService.generateJwt(SecurityContextHolder.getContext( ).getAuthentication( ));
    }
    
    public String getBearerToken( ) {
        return "Bearer " + getJwt( );
    }
}
