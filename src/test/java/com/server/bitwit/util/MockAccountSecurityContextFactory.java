package com.server.bitwit.util;

import com.server.bitwit.module.account.AccountService;
import com.server.bitwit.module.account.dto.CreateEmailAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@RequiredArgsConstructor
public class MockAccountSecurityContextFactory implements WithSecurityContextFactory<WithMockAccount> {
    
    private final UserDetailsService userDetailsService;
    private final AccountService     accountService;
    
    @Override
    public SecurityContext createSecurityContext(WithMockAccount annotation) {
        var username = annotation.username( );
        var email    = annotation.email( );
        var password = annotation.password( );
        
        var request  = new CreateEmailAccountRequest(username, email, password);
        accountService.createEmailAccount(request);
        
        var userDetails = userDetailsService.loadUserByUsername(email);
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword( ),
                userDetails.getAuthorities( )
        );
        var securityContext = SecurityContextHolder.createEmptyContext( );
        securityContext.setAuthentication(authenticationToken);
        return securityContext;
    }
}
