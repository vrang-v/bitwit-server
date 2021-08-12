package com.server.bitwit.module.security.oauth2;

import com.server.bitwit.module.security.AccountPrincipal;
import com.server.bitwit.domain.Account;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class OAuth2UserPrincipal extends DefaultOAuth2User implements AccountPrincipal {
    
    private final Account account;
    
    public OAuth2UserPrincipal(Account account, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey) {
        super(authorities, attributes, nameAttributeKey);
        this.account = account;
    }
}
