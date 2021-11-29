package com.server.bitwit.module.vote.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.bitwit.domain.VotingOption;
import com.server.bitwit.module.stock.dto.StockResponse;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

@Data
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
}
