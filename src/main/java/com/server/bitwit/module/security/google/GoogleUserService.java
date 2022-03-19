package com.server.bitwit.module.security.google;

import com.server.bitwit.domain.Account;
import com.server.bitwit.domain.Authority;
import com.server.bitwit.infra.client.google.dto.GoogleUser;
import com.server.bitwit.infra.storage.StorageService;
import com.server.bitwit.module.account.AccountRepository;
import com.server.bitwit.util.NamedByteArrayResource;
import com.server.bitwit.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

import static com.server.bitwit.domain.AccountType.GOOGLE;

@RequiredArgsConstructor
@Service
public class GoogleUserService {
    
    private final AccountRepository accountRepository;
    
    private final StorageService storageService;
    
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
        var account = accountRepository.findByEmailAndAccountType(googleUser.getEmail( ), GOOGLE)
                                       .map(_account -> _account.changeName(googleUser.getName( )))
                                       .map(_account -> _account.changeEmail(googleUser.getEmail( )))
                                       .orElseGet(( ) -> createGoogleAccount(googleUser));
        return accountRepository.save(account);
    }
    
    private Account createGoogleAccount(GoogleUser googleUser) {
        var response     = new RestTemplate( ).getForEntity(googleUser.getPicture( ), byte[].class);
        var fileName     = StringUtils.getLastElement(googleUser.getPicture( ), "/");
        var imageContent = response.getBody( );
        var uploadFile   = storageService.upload(new NamedByteArrayResource(fileName, imageContent));
        return Account.createOAuthAccount(googleUser.getName( ), googleUser.getEmail( ), GOOGLE)
                      .changeProfileImage(uploadFile);
    }
}
