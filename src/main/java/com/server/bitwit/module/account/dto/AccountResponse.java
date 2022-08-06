package com.server.bitwit.module.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.bitwit.domain.Account;
import com.server.bitwit.domain.AccountType;
import com.server.bitwit.infra.config.MapStructConfig;
import com.server.bitwit.module.file.mapper.UploadFileToUrlMapper;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

import static lombok.AccessLevel.PRIVATE;

@Data @FieldDefaults(level = PRIVATE)
public class AccountResponse {
    
    @JsonProperty("accountId")
    Long id;
    
    String name;
    
    String email;
    
    AccountType accountType;
    
    boolean emailVerified;
    
    String profileImage;
    
    
    @Mapper(config = MapStructConfig.class, uses = UploadFileToUrlMapper.class)
    public interface AccountResponseMapper extends Converter<Account, AccountResponse> { }
    
}
