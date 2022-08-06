package com.server.bitwit.module.ballot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.bitwit.domain.Ballot;
import com.server.bitwit.domain.VotingOption;
import com.server.bitwit.infra.config.MapStructConfig;
import com.server.bitwit.module.account.dto.AccountResponse;
import com.server.bitwit.module.account.dto.AccountResponse.AccountResponseMapper;
import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Data
public class BallotResponse {
    
    @JsonProperty("ballotId")
    Long id;
    
    Long voteId;
    
    AccountResponse account;
    
    VotingOption votingOption;
    
    String status;
    
    public BallotResponse withStatus(String status) {
        this.status = status;
        return this;
    }
    
    
    @Mapper(config = MapStructConfig.class, uses = AccountResponseMapper.class)
    public interface BallotResponseMapper extends Converter<Ballot, BallotResponse> {
        
        @Override
        @Mapping(target = "voteId", expression = "java(ballot.getVote().getId())")
        BallotResponse convert(Ballot ballot);
        
    }
    
}
