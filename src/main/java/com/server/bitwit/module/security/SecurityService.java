package com.server.bitwit.module.security;

import com.server.bitwit.infra.client.google.dto.GoogleUser;
import com.server.bitwit.module.security.google.GoogleLoginAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SecurityService {
    
    private final AuthenticationManager authenticationManager;
    
    public Authentication authenticate(Object principal, Object credentials) {
        var loginToken     = new UsernamePasswordAuthenticationToken(principal, credentials);
        var authentication = authenticationManager.authenticate(loginToken);
        SecurityContextHolder.getContext( ).setAuthentication(authentication);
        return authentication;
    }
    
    public Authentication authenticate(GoogleUser googleUser) {
        var token          = new GoogleLoginAuthenticationToken(googleUser);
        var authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext( ).setAuthentication(authentication);
        return authentication;
    }
}
