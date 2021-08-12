package com.server.bitwit.module.account.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UpdateAccountRequest
{
    private String name;
    
    @Email
    private String email;
    
    private String password;
}
