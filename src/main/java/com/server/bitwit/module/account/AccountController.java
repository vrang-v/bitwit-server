package com.server.bitwit.module.account;

import com.server.bitwit.module.account.dto.AccountResponse;
import com.server.bitwit.module.account.dto.CreateEmailAccountRequest;
import com.server.bitwit.module.account.dto.DuplicateCheckResponse;
import com.server.bitwit.module.account.dto.UpdateAccountRequest;
import com.server.bitwit.module.security.jwt.support.Jwt;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Email;

import static com.server.bitwit.domain.AccountType.EMAIL;
import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    
    private final AccountService accountService;
    
    @PostMapping
    @ResponseStatus(CREATED)
    public AccountResponse createAccount(@Valid @RequestBody CreateEmailAccountRequest request) {
        return accountService.createEmailAccount(request);
    }
    
    @PostMapping("/profile-image")
    public AccountResponse setProfileImage(@Jwt Long accountId, @RequestParam("file") MultipartFile file) {
        return accountService.changeProfileImage(accountId, file);
    }
    
    @GetMapping("/me")
    public AccountResponse getAccount(@Jwt Long accountId) {
        return accountService.getAccountResponse(accountId);
    }
    
    @GetMapping(path = "/me/profile-image", produces = "image/jpeg")
    public Resource getProfileImage(@Jwt Long accountId) {
        return accountService.getProfileImage(accountId);
    }
    
    @PatchMapping
    public AccountResponse updateAccount(@Jwt Long accountId, @Valid @RequestBody UpdateAccountRequest request) {
        return accountService.updateAccount(accountId, request);
    }
    
    @GetMapping("/duplicate-check/email")
    public DuplicateCheckResponse checkForDuplicateEmail(@Email @Validated @RequestParam String email) {
        return DuplicateCheckResponse.email(email, accountService.existsByEmailAndAccountType(email, EMAIL));
    }
    
    @GetMapping("/duplicate-check/name")
    public DuplicateCheckResponse checkForDuplicateName(@RequestParam String name) {
        return DuplicateCheckResponse.name(name, accountService.existsByName(name));
    }
    
    @GetMapping("/email-verified-check")
    public JSONObject checkEmailVerified(@RequestParam Long accountId) {
        return new JSONObject( )
                .put("accountId", accountId)
                .put("verified", accountService.isEmailVerified(accountId));
    }
    
    @GetMapping("/resend-email-token")
    public void resendEmailToken(@RequestParam Long accountId) {
        accountService.resendSignUpConfirmEmail(accountId);
    }
}
