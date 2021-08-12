package com.server.bitwit.module.account.dto;

import com.server.bitwit.domain.Account;
import lombok.Data;

@Data
public class AccountResponse
{
    Long id;
    
    String name;
    
    String email;
    
    public static AccountResponse fromAccount(Account account)
    {
        var response = new AccountResponse( );
        response.id    = account.getId( );
        response.name  = account.getName( );
        response.email = account.getEmail( );
        return response;
    }
}
