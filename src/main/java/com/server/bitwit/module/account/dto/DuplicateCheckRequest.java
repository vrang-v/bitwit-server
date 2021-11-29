package com.server.bitwit.module.account.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class DuplicateCheckRequest {
    
    @Email
    private String email;
    
    private String name;
}
