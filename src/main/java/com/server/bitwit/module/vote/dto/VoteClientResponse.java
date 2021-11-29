package com.server.bitwit.module.vote.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.bitwit.domain.VotingOption;
import com.server.bitwit.module.stock.dto.StockResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

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
}
