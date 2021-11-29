package com.server.bitwit.module.account.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.server.bitwit.module.common.dto.BitwitResponse;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class DuplicateCheckResponse implements BitwitResponse {
    
    String email;
    
    String name;
    
    boolean duplicate;
    
    public static DuplicateCheckResponse email(String email, boolean duplicate) {
        var response = new DuplicateCheckResponse( );
        response.setDuplicate(duplicate);
        response.setEmail(email);
        return response;
    }
    
    public static DuplicateCheckResponse name(String name, boolean duplicate) {
        var response = new DuplicateCheckResponse( );
        response.setDuplicate(duplicate);
        response.setEmail(name);
        return response;
    }
}
