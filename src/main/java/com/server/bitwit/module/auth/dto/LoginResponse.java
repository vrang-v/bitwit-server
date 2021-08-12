package com.server.bitwit.module.auth.dto;

import com.server.bitwit.domain.Account;
import lombok.Value;

@Value
public class LoginResponse
{
    String jwt;
    
    Long id;
    
    String name;
    
    String email;
    
    public LoginResponse(Account account, String jwt)
    {
        this.jwt   = jwt;
        this.id    = account.getId( );
        this.name  = account.getName( );
        this.email = account.getEmail( );
    }
}
