package com.server.bitwit.infra.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Profile("test")
@Service
public class ConsoleEmailService implements EmailService {
    
    @Override
    public void sendEmail(EmailMessage emailMessage) {
        log.info("===========================");
        log.info("Email has arrived: {}", emailMessage.getSubject( ));
        log.info("To. {}", emailMessage.getTo( ));
        log.info("message: {}", emailMessage.getMessage( ));
        log.info("===========================");
    }
}
