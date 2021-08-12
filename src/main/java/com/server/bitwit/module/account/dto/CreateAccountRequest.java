package com.server.bitwit.module.account.dto;

import com.server.bitwit.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest
{
    @NotBlank
    private String name;
    
    @Email
    private String email;
    
    @NotBlank
    private String password;
    
    public Account toAccount( )
    {
        return Account.createEmailAccount(name, email, password);
    }
}
