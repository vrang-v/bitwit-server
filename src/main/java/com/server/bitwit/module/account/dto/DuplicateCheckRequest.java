package com.server.bitwit.module.account.dto;

import lombok.Data;

@Data
public class DuplicateCheckRequest
{
    private String email;
    
    private String name;
}
