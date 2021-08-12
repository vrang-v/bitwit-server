package com.server.bitwit.module.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClaimProperties {
    
    public static String accountIdKey;
    
    public static String authoritiesKey;
    
    @Value("${jwt.claims.account-id-key}")
    public void setAccountIdKey(String accountIdKey) {
        ClaimProperties.accountIdKey = accountIdKey;
    }
    
    @Value("${jwt.claims.authorities-key}")
    public void setAuthoritiesKey(String authoritiesKey) {
        ClaimProperties.authoritiesKey = authoritiesKey;
    }
}
