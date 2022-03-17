package com.server.bitwit.infra.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Slf4j
@Profile("!test")
@RequiredArgsConstructor
@Service
public class HtmlEmailService implements EmailService {
    
    private final JavaMailSender mailSender;
    
    @Override
    public void sendEmail(EmailMessage emailMessage) {
        var mimeMessage = mailSender.createMimeMessage( );
        try {
            var mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo( ));
            mimeMessageHelper.setSubject(emailMessage.getSubject( ));
            mimeMessageHelper.setText(emailMessage.getMessage( ), true);
            mimeMessageHelper.setFrom("Bitwit");
            mailSender.send(mimeMessage);
            log.info("Email was sent successfully: {}", emailMessage.getMessage( ));
        }
        catch (MessagingException e) {
            log.error("Mail transmission failed.", e);
        }
    }
}
