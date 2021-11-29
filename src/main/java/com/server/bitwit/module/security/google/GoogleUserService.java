package com.server.bitwit.module.security.google;

import com.server.bitwit.domain.Account;
import com.server.bitwit.domain.AccountType;
import com.server.bitwit.domain.Authority;
import com.server.bitwit.infra.client.google.dto.GoogleUser;
import com.server.bitwit.module.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GoogleUserService {
    
    private final AccountRepository accountRepository;
    
    public Authentication loadUser(GoogleUser googleUser) {
        var account   = saveOrUpdate(googleUser);
        var principal = new GoogleLoginPrincipal(account);
        var authorities = account.getAuthorities( ).stream( )
                                 .map(Authority::getAuthorityName)
                                 .map(SimpleGrantedAuthority::new)
                                 .collect(Collectors.toList( ));
        
        return new GoogleLoginAuthenticationToken(principal, authorities);
    }
    
    private Account saveOrUpdate(GoogleUser googleUser) {
        var account = accountRepository.findByEmailAndAccountType(googleUser.getEmail( ), AccountType.GOOGLE)
                                       .map(_account -> _account.changeName(googleUser.getName( )))
                                       .map(_account -> _account.changeEmail(googleUser.getEmail( )))
                                       .orElseGet(( ) -> createGoogleAccount(googleUser));
        return accountRepository.save(account);
    }
    
    private Account createGoogleAccount(GoogleUser googleUser) {
        return Account.createOAuthAccount(googleUser.getName( ), googleUser.getEmail( ), AccountType.GOOGLE);
    }
    
}
