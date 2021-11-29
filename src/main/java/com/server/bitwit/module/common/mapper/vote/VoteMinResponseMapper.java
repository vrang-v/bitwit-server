package com.server.bitwit.module.common.mapper.vote;

import com.server.bitwit.domain.Vote;
import com.server.bitwit.module.common.mapper.MapStructConfig;
import com.server.bitwit.module.common.mapper.stock.StockResponseMapper;
import com.server.bitwit.module.vote.dto.VoteMinResponse;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(config = MapStructConfig.class, uses = StockResponseMapper.class, nullValuePropertyMappingStrategy = IGNORE)
public interface VoteMinResponseMapper extends Converter<Vote, VoteMinResponse> { }
