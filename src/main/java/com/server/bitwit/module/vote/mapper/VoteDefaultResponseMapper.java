package com.server.bitwit.module.vote.mapper;

import com.server.bitwit.domain.Vote;
import com.server.bitwit.infra.config.MapStructConfig;
import com.server.bitwit.module.ballot.mapper.BallotResponseMapper;
import com.server.bitwit.module.stock.mapper.StockResponseMapper;
import com.server.bitwit.module.vote.dto.VoteDefaultResponse;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class, uses = {StockResponseMapper.class, BallotResponseMapper.class})
public interface VoteDefaultResponseMapper extends Converter<Vote, VoteDefaultResponse> {
}
