package com.server.bitwit.infra.email;

import com.server.bitwit.infra.email.dto.SimpleLinkEmailRequest;
import com.server.bitwit.infra.properties.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RequiredArgsConstructor
@Service
public class TemplateEmailService {
    
    private final EmailService emailService;
    
    private final AppProperties appProperties;
    
    private final TemplateEngine templateEngine;
    
    @Async
    public void sendSimpleLinkEmail(String to, String subject, SimpleLinkEmailRequest request) {
        var context = new Context( );
        context.setVariable("link", request.getLink( ));
        context.setVariable("linkName", request.getLinkName( ));
        context.setVariable("nickname", request.getNickname( ));
        context.setVariable("message", request.getMessage( ));
        context.setVariable("host", appProperties.getHost( ));
        var message = templateEngine.process("mail/simple-link", context);
        emailService.sendEmail(to, subject, message);
    }
}
