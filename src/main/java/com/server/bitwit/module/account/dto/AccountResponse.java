package com.server.bitwit.module.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.bitwit.domain.AccountType;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data @FieldDefaults(level = PRIVATE)
public class AccountResponse {
    
    @JsonProperty("accountId")
    Long id;
    
    String name;
    
    String email;
    
    AccountType accountType;
    
    boolean emailVerified;
}
