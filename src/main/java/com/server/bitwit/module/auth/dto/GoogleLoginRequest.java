package com.server.bitwit.module.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GoogleLoginRequest {
    
    @NotBlank
    private String idToken;
    
}
