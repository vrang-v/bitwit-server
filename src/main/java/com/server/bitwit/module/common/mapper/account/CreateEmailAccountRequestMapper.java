package com.server.bitwit.module.common.mapper.account;

import com.server.bitwit.domain.Account;
import com.server.bitwit.module.account.dto.CreateEmailAccountRequest;
import com.server.bitwit.module.common.mapper.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class)
public interface CreateEmailAccountRequestMapper extends Converter<CreateEmailAccountRequest, Account> {
    
    @ObjectFactory
    default Account createAccount(CreateEmailAccountRequest request) {
        return Account.createEmailAccount(request.getName( ), request.getEmail( ), request.getPassword( ));
    }
    
}
