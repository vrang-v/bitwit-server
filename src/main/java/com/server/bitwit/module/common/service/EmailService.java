package com.server.bitwit.module.common.service;

import com.server.bitwit.domain.Account;
import com.server.bitwit.infra.email.TemplateEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.server.bitwit.infra.email.dto.SimpleLinkEmailRequest.createSimpleLinkEmailRequest;

@RequiredArgsConstructor
@Service
public class EmailService {
    
    private final TemplateEmailService templateEmailService;
    
    public Account sendSignUpConfirmEmail(Account account) {
        account.generateEmailToken( );
        var to      = account.getEmail( );
        var subject = "회원가입 인증 메일입니다";
        
        var link = "/accounts/accept" +
                   "?token=" + account.getEmailToken( ) +
                   "&email=" + account.getEmail( );
        var linkName = "링크를 눌러 이메일 인증하기";
        var message  = "서비스를 정상적으로 사용하기 위해서는 인증이 필요합니다.";
        
        var request = createSimpleLinkEmailRequest(link, linkName, account.getEmail( ), message);
        templateEmailService.sendSimpleLinkEmail(to, subject, request);
        return account;
    }
    
}
