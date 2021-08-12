package com.server.bitwit.module.security.oauth2.attributes;

import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.domain.Account;
import com.server.bitwit.domain.AccountType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class OAuth2Attributes {
    
    public static final String GOOGLE = "google";
    public static final String NAVER  = "naver";
    
    protected String              nameAttributeKey;
    protected Map<String, Object> attributes;
    
    public static OAuth2Attributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        switch (registrationId) {
            case GOOGLE:
                return new GoogleOAuth2Attributes(userNameAttributeName, attributes);
            case NAVER:
                return new NaverOAuth2Attributes("id", attributes);
            default:
                throw new BitwitException(registrationId + " 로그인은 지원하지 않습니다.");
        }
    }
    
    public abstract String getId( );
    
    public abstract String getName( );
    
    public abstract String getEmail( );
    
    public abstract String getPicture( );
    
    public abstract AccountType getAccountType( );
    
    public Account toAccount( ) {
        return Account.createOAuthAccount(getName( ), getEmail( ), getAccountType( ));
    }
}
