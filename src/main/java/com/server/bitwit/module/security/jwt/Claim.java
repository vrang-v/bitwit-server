package com.server.bitwit.module.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Claim {
    
    ACCOUNT_ID(ClaimProperties.accountIdKey, Long.class),
    AUTHORITIES(ClaimProperties.authoritiesKey, String.class);
    
    private String   key;
    private Class<?> type;
}
