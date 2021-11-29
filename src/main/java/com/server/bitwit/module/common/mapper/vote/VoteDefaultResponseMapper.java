package com.server.bitwit.module.common.mapper.vote;

import com.server.bitwit.domain.Vote;
import com.server.bitwit.module.common.mapper.MapStructConfig;
import com.server.bitwit.module.common.mapper.ballot.BallotResponseMapper;
import com.server.bitwit.module.common.mapper.stock.StockResponseMapper;
import com.server.bitwit.module.vote.dto.VoteDefaultResponse;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class, uses = {StockResponseMapper.class, BallotResponseMapper.class})
public interface VoteDefaultResponseMapper extends Converter<Vote, VoteDefaultResponse> {
}
