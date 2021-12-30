package com.server.bitwit.module.account.mapper;

import com.server.bitwit.domain.Account;
import com.server.bitwit.module.account.AccountService;
import com.server.bitwit.infra.config.MapStructConfig;
import com.server.bitwit.module.error.exception.NonExistentResourceException;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class)
public abstract class AccountIdMapper implements Converter<Long, Account> {
    
    @Lazy @Autowired AccountService accountService;
    
    @ObjectFactory
    public Account getAccount(Long accountId) {
        return accountService.findById(accountId)
                             .orElseThrow(( ) -> new NonExistentResourceException("account", accountId));
    }
    
}
