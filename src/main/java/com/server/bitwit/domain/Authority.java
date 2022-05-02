package com.server.bitwit.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Authority {
    
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");
    
    private final String authorityName;
}
