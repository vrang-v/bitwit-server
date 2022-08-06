package com.server.bitwit.module.account.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailVerifiedCheckResponse {
    
    private Long accountId;
    
    private Boolean verified;
    
}
