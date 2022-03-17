package com.server.bitwit.module.account;

import com.server.bitwit.domain.Account;
import com.server.bitwit.domain.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Optional<Account> findByName(String name);
    
    Optional<Account> findByEmail(String email);
    
    Optional<Account> findByEmailAndAccountType(String email, AccountType accountType);
    
    boolean existsByEmailAndAccountType(String email, AccountType accountType);
    
    boolean existsByName(String name);
    
    void deleteByEmail(String email);
}
