package com.server.bitwit.dto.response;

import com.server.bitwit.domain.Ballot;
import com.server.bitwit.domain.Stock;
import com.server.bitwit.domain.Vote;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VoteResponse
{
    Long id;
    
    Stock stock;
    
    List<Ballot> ballots;
    
    String description;
    
    LocalDateTime startAt;
    
    LocalDateTime endedAt;
    
    Instant createdAt;
    
    Instant updatedAt;
    
    public static VoteResponse fromVote(Vote vote)
    {
        var response = new VoteResponse( );
        response.id          = vote.getId( );
        response.stock       = vote.getStock( );
        response.ballots     = vote.getBallots( );
        response.description = vote.getDescription( );
        response.startAt     = vote.getStartAt( );
        response.endedAt     = vote.getEndedAt( );
        response.createdAt   = vote.getCreatedAt( );
        response.updatedAt   = vote.getUpdatedAt( );
        return response;
    }
}
