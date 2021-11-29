package com.server.bitwit.infra.email;

public interface EmailService {
    
    void sendEmail(EmailMessage emailMessage);
    
    default void sendEmail(String to, String subject, String message) {
        sendEmail(new EmailMessage(to, subject, message));
    }

}
