package com.server.bitwit.module.security.oauth2.attributes;

import com.server.bitwit.domain.AccountType;

import java.util.Map;

public class GoogleOAuth2Attributes extends OAuth2Attributes {
    
    public GoogleOAuth2Attributes(String nameAttributeKey, Map<String, Object> attributes) {
        super(nameAttributeKey, attributes);
    }
    
    @Override
    public String getId( ) {
        return (String)attributes.get(nameAttributeKey);
    }
    
    @Override
    public String getName( ) {
        return (String)attributes.get("name");
    }
    
    @Override
    public String getEmail( ) {
        return (String)attributes.get("email");
    }
    
    @Override
    public String getPicture( ) {
        return (String)attributes.get("picture");
    }
    
    @Override
    public AccountType getAccountType( ) {
        return AccountType.GOOGLE;
    }
    
}
