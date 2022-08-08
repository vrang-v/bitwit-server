package com.server.bitwit.module.security.google;

import com.server.bitwit.infra.client.google.dto.GoogleUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
public class GoogleLoginAuthenticationToken extends AbstractAuthenticationToken {
    
    private transient GoogleUser googleUser;
    
    private transient GoogleLoginPrincipal principal;
    
    public GoogleLoginAuthenticationToken(GoogleUser googleUser) {
        super(null);
        this.googleUser = googleUser;
        this.setAuthenticated(false);
    }
    
    public GoogleLoginAuthenticationToken(GoogleLoginPrincipal principal, List<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.setAuthenticated(true);
    }
    
    @Override
    public Object getPrincipal( ) {
        return principal;
    }
    
    @Override
    public Object getCredentials( ) {
        return "";
    }
}
