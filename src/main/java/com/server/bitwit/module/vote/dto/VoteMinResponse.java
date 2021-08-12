package com.server.bitwit.module.vote.dto;

import com.server.bitwit.domain.Stock;
import com.server.bitwit.domain.Vote;
import com.server.bitwit.domain.VotingOption;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class VoteMinResponse implements VoteResponse
{
    Long id;
    
    Stock stock;
    
    String description;
    
    LocalDateTime startAt;
    
    LocalDateTime endedAt;
    
    Instant createdAt;
    
    Instant updatedAt;
    
    int participantCount;
    
    Map<VotingOption, Integer> selectionCount;
    
    public static VoteMinResponse fromVote(Vote vote)
    {
        var response = new VoteMinResponse( );
        response.id                = vote.getId( );
        response.stock             = vote.getStock( );
        response.description       = vote.getDescription( );
        response.startAt           = vote.getStartAt( );
        response.endedAt           = vote.getEndedAt( );
        response.createdAt         = vote.getCreatedAt( );
        response.updatedAt        = vote.getUpdatedAt( );
        response.participantCount = vote.getParticipantsCount( );
        response.selectionCount   = vote.getSelectionCount( );
        return response;
    }
}
