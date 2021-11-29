package com.server.bitwit.module.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.bitwit.domain.Account;
import lombok.Value;

@Value
public class LoginResponse {
    
    String jwt;
    
    @JsonProperty("accountId")
    Long id;
    
    String name;
    
    String email;
    
    Boolean emailVerified;
    
    public LoginResponse(Account account, String jwt) {
        this.jwt           = jwt;
        this.id            = account.getId( );
        this.name          = account.getName( );
        this.email         = account.getEmail( );
        this.emailVerified = account.isEmailVerified( );
    }
}
