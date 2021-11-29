package com.server.bitwit.infra.email;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data @FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailMessage {
    
    String to;
    
    String subject;
    
    String message;
    
    public EmailMessage(String to, String subject, String message) {
        this.to      = to;
        this.subject = "[Bitwit] " + subject;
        this.message = message;
    }
}
