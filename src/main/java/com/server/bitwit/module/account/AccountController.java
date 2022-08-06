package com.server.bitwit.module.account;

import com.server.bitwit.module.account.dto.*;
import com.server.bitwit.module.error.exception.InvalidRequestException;
import com.server.bitwit.module.security.jwt.support.Jwt;
import com.server.bitwit.util.FormatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

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
    public AccountResponse setProfileImage(@Jwt Long accountId, @RequestParam("profileImage") MultipartFile profileImage)
    throws IOException {
        if (profileImage.isEmpty( )) {
            throw new InvalidRequestException("프로필 이미지가 비어있습니다.");
        }
        var imageName    = profileImage.getOriginalFilename( );
        var imageContent = profileImage.getBytes( );
        return accountService.changeProfileImage(accountId, imageName, imageContent);
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
    public DuplicateCheckResponse checkForDuplicateEmail(@RequestParam String email) {
        if (! FormatUtils.isEmailFormat(email)) {
            throw new InvalidRequestException("올바른 이메일 형식이 아닙니다.");
        }
        var result = accountService.existsByEmailAndAccountType(email, EMAIL);
        return DuplicateCheckResponse.email(email, result);
    }
    
    @GetMapping("/duplicate-check/name")
    public DuplicateCheckResponse checkForDuplicateName(@RequestParam String name) {
        var result = accountService.existsByName(name);
        return DuplicateCheckResponse.name(name, result);
    }
    
    @GetMapping("/email-verified-check")
    public EmailVerifiedCheckResponse checkEmailVerified(@RequestParam Long accountId) {
        return EmailVerifiedCheckResponse.builder( )
                                         .accountId(accountId)
                                         .verified(accountService.isEmailVerified(accountId))
                                         .build( );
    }
    
    @GetMapping("/resend-email-token")
    public void resendEmailToken(@RequestParam Long accountId) {
        accountService.resendSignUpConfirmEmail(accountId);
    }
}
