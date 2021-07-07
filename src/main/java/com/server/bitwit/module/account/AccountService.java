package com.server.bitwit.module.account;

import com.server.bitwit.module.account.dto.AccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AccountService
{
    private final AccountRepository accountRepository;
    
    @Transactional
    public void createAccount(AccountRequest request)
    {
        accountRepository.save(request.toAccount( ));
    }
    
    public boolean existsById(Long accountId)
    {
        return accountRepository.existsById(accountId);
    }
}
