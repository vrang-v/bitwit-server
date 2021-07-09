package com.server.bitwit.module.account;

import com.server.bitwit.module.account.dto.AccountRequest;
import com.server.bitwit.module.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AccountService
{
    private final AccountRepository accountRepository;
    
    @Transactional
    public Long createAccount(AccountRequest request)
    {
        return accountRepository.save(request.toAccount( )).getId( );
    }
    
    public Optional<Account> getAccount(Long accountId)
    {
        return accountRepository.findById(accountId);
    }
    
    public boolean existsById(Long accountId)
    {
        return accountRepository.existsById(accountId);
    }
}
