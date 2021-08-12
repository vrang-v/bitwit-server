package com.server.bitwit.module.account;

import com.server.bitwit.module.security.jwt.Jwt;
import com.server.bitwit.module.account.dto.*;
import com.server.bitwit.module.common.dto.SimpleIdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {
    
    private final AccountService accountService;
    
    @PostMapping
    @ResponseStatus(CREATED)
    public SimpleIdResponse createAccount(@Valid @RequestBody CreateAccountRequest request) {
        var accountId = accountService.createAccount(request);
        return SimpleIdResponse.of(accountId);
    }
    
    @GetMapping("/me")
    public AccountResponse getAccount(@Jwt Long accountId) {
        return accountService.getAccountResponse(accountId);
    }
    
    @PatchMapping
    @ResponseStatus(NO_CONTENT)
    public void updateAccount(@Jwt Long accountId, @Valid @RequestBody UpdateAccountRequest request) {
        accountService.updateAccount(accountId, request);
    }
    
    @PostMapping("/duplicate-check/email")
    public DuplicateCheckResponse checkForDuplicateNickname(@RequestBody DuplicateCheckRequest request) {
        var result = accountService.existsByEmail(request.getEmail( ));
        return new DuplicateCheckResponse(result);
    }
}
