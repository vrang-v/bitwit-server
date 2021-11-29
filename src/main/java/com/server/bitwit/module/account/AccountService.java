package com.server.bitwit.module.account;

import com.server.bitwit.domain.Account;
import com.server.bitwit.domain.AccountType;
import com.server.bitwit.module.account.dto.AcceptSignUpRequest;
import com.server.bitwit.module.account.dto.AccountResponse;
import com.server.bitwit.module.account.dto.CreateEmailAccountRequest;
import com.server.bitwit.module.account.dto.UpdateAccountRequest;
import com.server.bitwit.module.common.service.EmailService;
import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.module.error.exception.ErrorCode;
import com.server.bitwit.module.error.exception.NonExistentResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.time.LocalDateTime.now;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AccountService {
    
    private final AccountRepository accountRepository;
    
    private final ConversionService conversionService;
    private final EmailService      emailService;
    
    @Transactional
    public AccountResponse createEmailAccount(CreateEmailAccountRequest request) {
        return Optional.ofNullable(conversionService.convert(request, Account.class))
                       .map(emailService::sendSignUpConfirmEmail)
                       .map(accountRepository::save)
                       .map(account -> conversionService.convert(account, AccountResponse.class))
                       .orElse(null);
    }
    
    public AccountResponse getAccountResponse(Long accountId) {
        return accountRepository.findById(accountId)
                                .map(account -> conversionService.convert(account, AccountResponse.class))
                                .orElseThrow(( ) -> new NonExistentResourceException("account", accountId));
    }
    
    @Transactional
    public AccountResponse updateAccount(Long accountId, UpdateAccountRequest request) {
        var account = accountRepository.findById(accountId)
                                       .orElseThrow(( ) -> new NonExistentResourceException("account", accountId))
                                       .changeName(request.getName( ))
                                       .changeEmail(request.getEmail( ))
                                       .changePassword(request.getPassword( ));
        return conversionService.convert(account, AccountResponse.class);
    }
    
    @Transactional
    public Account acceptSignUp(AcceptSignUpRequest request) {
        var email = request.getEmail( );
        var token = request.getToken( );
        return accountRepository.findByEmailAndAccountType(email, AccountType.EMAIL)
                                .filter(account -> token.equals(account.getEmailToken( )))
                                .map(Account::verify)
                                .orElse(null);
    }
    
    public boolean isEmailVerified(Long accountId) {
        return accountRepository.findById(accountId)
                                .filter(Account::isEmailVerified)
                                .isPresent( );
    }
    
    @Transactional
    public void resendSignUpConfirmEmail(Long accountId) {
        accountRepository.findById(accountId)
                         .filter(account -> account.getAccountType( ) == AccountType.EMAIL)
                         .ifPresent(account -> {
                             var lastEmailSentAt                = account.getLastEmailTokenGeneratedAt( );
                             var lastEmailSentLessThanOneMinute = lastEmailSentAt.plusMinutes(1L).isAfter(now( ));
                             if (lastEmailSentLessThanOneMinute) {
                                 throw new BitwitException(ErrorCode.REQUEST_LIMIT);
                             }
                             emailService.sendSignUpConfirmEmail(account);
                         });
    }
    
    public Optional<Account> findById(Long accountId) {
        return accountRepository.findById(accountId);
    }
    
    public boolean existsById(Long accountId) {
        return accountRepository.existsById(accountId);
    }
    
    public boolean existsByEmailAndAccountType(String email, AccountType accountType) {
        return accountRepository.existsByEmailAndAccountType(email, accountType);
    }
    
    public boolean existsByName(String name) {
        return accountRepository.existsByName(name);
    }
}
