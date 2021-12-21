package com.server.bitwit.module.ballot.search;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class BallotSearchCondition {
    
    Long accountId;
    
    Long voteId;
    
    Long stockId;
    
    String ticker;
    
    Instant before;
    
    Instant after;
    
    Instant createdAt;
    
}
