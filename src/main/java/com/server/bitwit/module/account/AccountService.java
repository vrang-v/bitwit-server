package com.server.bitwit.module.account;

import com.server.bitwit.module.account.dto.AccountResponse;
import com.server.bitwit.module.account.dto.CreateAccountRequest;
import com.server.bitwit.module.account.dto.UpdateAccountRequest;
import com.server.bitwit.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AccountService {
    
    private final AccountRepository accountRepository;
    
    @Transactional
    public Long createAccount(CreateAccountRequest request) {
        return accountRepository.save(request.toAccount( )).getId( );
    }
    
    public Optional<Account> getAccount(Long accountId) {
        return accountRepository.findById(accountId);
    }
    
    public AccountResponse getAccountResponse(Long accountId) {
        return getAccount(accountId).map(AccountResponse::fromAccount).orElseThrow( );
    }
    
    @Transactional
    public void updateAccount(Long accountId, UpdateAccountRequest request) {
        accountRepository.findById(accountId).orElseThrow( )
                         .changeName(request.getName( ))
                         .changePassword(request.getPassword( ))
                         .changeEmail(request.getEmail( ));
    }
    
    public boolean existsById(Long accountId) {
        return accountRepository.existsById(accountId);
    }
    
    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }
}
