package com.server.bitwit.module.security.google;

import com.server.bitwit.domain.Account;
import com.server.bitwit.module.security.AccountPrincipal;
import lombok.Getter;

@Getter
public class GoogleLoginPrincipal implements AccountPrincipal {
    
    private final Account account;
    
    public GoogleLoginPrincipal(Account account) {
        this.account = account;
    }
    
}
