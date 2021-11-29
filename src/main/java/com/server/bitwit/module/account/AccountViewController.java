package com.server.bitwit.module.account;

import com.server.bitwit.module.account.dto.AcceptSignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/accounts")
public class AccountViewController {
    
    private final AccountService accountService;
    
    @GetMapping("/accept")
    public String acceptSignUp(AcceptSignUpRequest request, Model model) {
        var account = accountService.acceptSignUp(request);
        
        if (account == null) {
            return "error";
        }
        model.addAttribute("nickname", account.getEmail( ));
        return "account/accept";
    }
}
