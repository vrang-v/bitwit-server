package com.server.bitwit.module.vote.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.bitwit.domain.VotingOption;
import com.server.bitwit.module.ballot.dto.BallotResponse;
import com.server.bitwit.module.stock.dto.StockResponse;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class VoteItemResponse implements VoteResponse {
    
    @JsonProperty("voteId")
    Long id;
    
    StockResponse stock;
    
    String description;
    
    LocalDateTime startAt;
    
    LocalDateTime endedAt;
    
    int participantsCount;
    
    BallotResponse ballot;
    
    Map<VotingOption, Integer> selectionCount;
}
