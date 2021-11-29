package com.server.bitwit.module.security.google;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class GoogleLoginAuthenticationProvider implements AuthenticationProvider {
    
    private final GoogleUserService googleUserService;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var googleUser = ((GoogleLoginAuthenticationToken)authentication).getGoogleUser( );
        return googleUserService.loadUser(googleUser);
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return GoogleLoginAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
