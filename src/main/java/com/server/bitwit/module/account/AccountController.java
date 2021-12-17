package com.server.bitwit.module.account;

import com.server.bitwit.domain.AccountType;
import com.server.bitwit.module.account.dto.*;
import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.module.error.exception.ErrorCode;
import com.server.bitwit.module.security.jwt.support.Jwt;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.MalformedURLException;

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
    public Resource getProfileImage(@Jwt Long accountId) throws MalformedURLException {
        return accountService.getProfileImage(accountId);
    }
    
    @PatchMapping
    public AccountResponse updateAccount(@Jwt Long accountId, @Valid @RequestBody UpdateAccountRequest request) {
        return accountService.updateAccount(accountId, request);
    }
    
    @GetMapping("/duplicate-check")
    public DuplicateCheckResponse checkForDuplicateEmail(@Valid @ModelAttribute DuplicateCheckRequest request) {
        boolean result;
        if (request.getEmail( ) != null) {
            result = accountService.existsByEmailAndAccountType(request.getEmail( ), AccountType.EMAIL);
            return DuplicateCheckResponse.email(request.getEmail( ), result);
        }
        if (request.getName( ) != null) {
            result = accountService.existsByName(request.getName( ));
            return DuplicateCheckResponse.name(request.getName( ), result);
        }
        throw new BitwitException(ErrorCode.INVALID_REQUEST);
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
