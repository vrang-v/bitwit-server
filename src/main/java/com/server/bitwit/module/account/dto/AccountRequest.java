package com.server.bitwit.module.account.dto;

import com.server.bitwit.module.domain.Account;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class AccountRequest
{
    @NotBlank
    private String name;
    
    @Email
    private String email;
    
    @NotBlank
    private String password;
    
    public Account toAccount( )
    {
        return Account.createAccount(name, email, password);
    }
}
