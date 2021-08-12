package com.server.bitwit.module.vote.dto;

import com.server.bitwit.domain.Vote;
import com.server.bitwit.domain.VotingOption;
import com.server.bitwit.module.stock.dto.StockDefaultResponse;
import com.server.bitwit.module.stock.dto.StockResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoteClientResponse implements VoteResponse
{
    Long voteId;
    
    String description;
    
    StockResponse stock;
    
    LocalDateTime startAt;
    
    LocalDateTime endedAt;
    
    int participantCount;
    
    Map<VotingOption, Integer> selectionCount;
    
    public static VoteClientResponse fromVote(Vote vote)
    {
        var response = new VoteClientResponse( );
        response.voteId            = vote.getId( );
        response.description       = vote.getDescription( );
        response.stock             = StockDefaultResponse.fromStock(vote.getStock( ));
        response.startAt           = vote.getStartAt( );
        response.endedAt          = vote.getEndedAt( );
        response.participantCount = vote.getParticipantsCount( );
        response.selectionCount   = vote.getSelectionCount( );
        return response;
    }
}
