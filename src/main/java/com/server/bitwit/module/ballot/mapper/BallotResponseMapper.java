package com.server.bitwit.module.ballot.mapper;

import com.server.bitwit.domain.Ballot;
import com.server.bitwit.module.ballot.dto.BallotResponse;
import com.server.bitwit.infra.config.MapStructConfig;
import com.server.bitwit.module.account.mapper.AccountResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class, uses = AccountResponseMapper.class)
public interface BallotResponseMapper extends Converter<Ballot, BallotResponse> {
    
    @Override
    @Mapping(target = "voteId", expression = "java(ballot.getVote().getId())")
    BallotResponse convert(Ballot ballot);
    
}
