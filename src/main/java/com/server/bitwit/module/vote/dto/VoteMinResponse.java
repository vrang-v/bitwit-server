package com.server.bitwit.module.vote.dto;

import com.server.bitwit.module.domain.Stock;
import com.server.bitwit.module.domain.Vote;
import com.server.bitwit.module.domain.VotingOption;
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
    
    int participantsCount;
    
    Map<VotingOption, Integer> selectionsCount;
    
    public static VoteMinResponse fromVote(Vote vote)
    {
        var response = new VoteMinResponse( );
        response.id                = vote.getId( );
        response.stock             = vote.getStock( );
        response.description       = vote.getDescription( );
        response.startAt           = vote.getStartAt( );
        response.endedAt           = vote.getEndedAt( );
        response.createdAt         = vote.getCreatedAt( );
        response.updatedAt         = vote.getUpdatedAt( );
        response.participantsCount = vote.getParticipantsCount( );
        response.selectionsCount   = vote.getSelectionsCount( );
        return response;
    }
}
