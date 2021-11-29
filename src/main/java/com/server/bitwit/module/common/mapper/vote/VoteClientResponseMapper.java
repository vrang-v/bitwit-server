package com.server.bitwit.module.common.mapper.vote;

import com.server.bitwit.domain.Vote;
import com.server.bitwit.module.common.mapper.MapStructConfig;
import com.server.bitwit.module.common.mapper.stock.StockResponseMapper;
import com.server.bitwit.module.vote.dto.VoteClientResponse;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class, uses = StockResponseMapper.class)
public interface VoteClientResponseMapper extends Converter<Vote, VoteClientResponse> { }
