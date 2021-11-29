package com.server.bitwit.module.common.mapper.account;

import com.server.bitwit.domain.Account;
import com.server.bitwit.module.account.dto.AccountResponse;
import com.server.bitwit.module.common.mapper.MapStructConfig;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class)
public interface AccountResponseMapper extends Converter<Account, AccountResponse> { }
