package com.server.bitwit.module.security.oauth2;

import com.server.bitwit.module.security.oauth2.attributes.OAuth2Attributes;
import com.server.bitwit.module.account.AccountRepository;
import com.server.bitwit.domain.Account;
import com.server.bitwit.domain.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
    
    private final AccountRepository accountRepository;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        var oAuth2User = super.loadUser(request);
        
        var clientRegistration = request.getClientRegistration( );
        var registrationId     = clientRegistration.getRegistrationId( );
        var userNameAttributeName = clientRegistration.getProviderDetails( )
                                                      .getUserInfoEndpoint( )
                                                      .getUserNameAttributeName( );
        
        var oAuthAttributes = OAuth2Attributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes( ));
        
        var account = saveOrUpdate(oAuthAttributes);
        
        var authorities = account.getAuthorities( ).stream( )
                                 .map(Authority::getAuthorityName)
                                 .collect(Collectors.joining(","));
        return new OAuth2UserPrincipal(
                account,
                Collections.singleton(new SimpleGrantedAuthority(authorities)),
                oAuthAttributes.getAttributes( ),
                oAuthAttributes.getNameAttributeKey( )
        );
    }
    
    private Account saveOrUpdate(OAuth2Attributes attributes) {
        var account = accountRepository.findByEmailAndAccountType(attributes.getEmail( ), attributes.getAccountType( ))
                                       .map(_account -> _account.changeName(attributes.getName( )))
                                       .map(_account -> _account.changeEmail(attributes.getEmail( )))
                                       .orElse(attributes.toAccount( ));
        return accountRepository.save(account);
    }
}
