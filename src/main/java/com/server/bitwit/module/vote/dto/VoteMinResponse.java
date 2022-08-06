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

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoteMinResponse implements VoteResponse {
    
    @JsonProperty("voteId")
    Long id;
    
    StockResponse stock;
    
    String description;
    
    LocalDateTime startAt;
    
    LocalDateTime endedAt;
    
    Instant createdAt;
    
    Instant updatedAt;
    
    int participantsCount;
    
    Map<VotingOption, Integer> selectionCount;
    
    
    @Mapper(config = MapStructConfig.class, uses = StockResponseMapper.class, nullValuePropertyMappingStrategy = IGNORE)
    public interface VoteMinResponseMapper extends Converter<Vote, VoteMinResponse> { }
    
}
