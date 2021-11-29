package com.server.bitwit.module.security.basic;

import com.server.bitwit.module.security.AccountPrincipal;
import com.server.bitwit.domain.Account;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@EqualsAndHashCode(callSuper = true)
public class UserDetailsPrincipal extends User implements AccountPrincipal {
    
    private final transient Account account;
    
    public UserDetailsPrincipal(Account account, Collection<? extends GrantedAuthority> authorities) {
        super(account.getName( ), account.getPassword( ), authorities);
        this.account = account;
    }
}
