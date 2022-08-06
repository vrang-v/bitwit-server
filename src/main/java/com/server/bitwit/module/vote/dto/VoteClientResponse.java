package com.server.bitwit.module.vote.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.bitwit.domain.Vote;
import com.server.bitwit.domain.VotingOption;
import com.server.bitwit.infra.config.MapStructConfig;
import com.server.bitwit.module.stock.dto.StockResponse;
import com.server.bitwit.module.stock.dto.StockResponse.StockResponseMapper;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoteClientResponse implements VoteResponse {
    
    @JsonProperty("voteId")
    Long id;
    
    String description;
    
    StockResponse stock;
    
    LocalDateTime startAt;
    
    LocalDateTime endedAt;
    
    int participantsCount;
    
    Map<VotingOption, Integer> selectionCount;
    
    
    @Mapper(config = MapStructConfig.class, uses = StockResponseMapper.class)
    public interface VoteClientResponseMapper extends Converter<Vote, VoteClientResponse> { }
    
}
