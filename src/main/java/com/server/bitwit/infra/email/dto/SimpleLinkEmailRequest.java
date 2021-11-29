package com.server.bitwit.infra.email.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class SimpleLinkEmailRequest {
    
    String link;
    String linkName;
    String nickname;
    String message;
    
    public static SimpleLinkEmailRequest createSimpleLinkEmailRequest(String link, String linkName, String nickname, String message) {
        return new SimpleLinkEmailRequest(link, linkName, nickname, message);
    }
    
}
