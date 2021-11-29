package com.server.bitwit.module.account.dto;

import lombok.Data;

@Data
public class AcceptSignUpRequest {
    
    String email;
    
    String token;
    
}
