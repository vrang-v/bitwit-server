package com.server.bitwit.module.security.google;

import com.server.bitwit.domain.Account;
import com.server.bitwit.domain.Authority;
import com.server.bitwit.infra.client.google.dto.GoogleUser;
import com.server.bitwit.module.account.AccountRepository;
import com.server.bitwit.module.file.FileStorage;
import com.server.bitwit.util.ImageFile;
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
    
    private final FileStorage fileStorage;
    
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
        var contentType  = response.getHeaders( ).getContentType( ).toString( );
        var imageContent = response.getBody( );
        var imageFile    = new ImageFile(imageContent, fileName, contentType);
        var uploadFile   = fileStorage.upload(imageFile);
        return Account.createOAuthAccount(googleUser.getName( ), googleUser.getEmail( ), GOOGLE)
                      .changeProfileImage(uploadFile);
    }
}
