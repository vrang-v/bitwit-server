package com.server.bitwit.module.vote.dto;

import com.server.bitwit.module.ballot.dto.BallotDefaultResponse;
import com.server.bitwit.module.ballot.dto.BallotResponse;
import com.server.bitwit.domain.Vote;
import com.server.bitwit.domain.VotingOption;
import com.server.bitwit.module.stock.dto.StockDefaultResponse;
import com.server.bitwit.module.stock.dto.StockResponse;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class VoteDefaultResponse implements VoteResponse
{
    Long id;
    
    StockResponse stock;
    
    List<BallotResponse> ballots;
    
    String description;
    
    LocalDateTime startAt;
    
    LocalDateTime endedAt;
    
    Instant createdAt;
    
    Instant updatedAt;
    
    int participantCount;
    
    Map<VotingOption, Integer> selectionCount;
    
    public static VoteResponse fromVote(Vote vote)
    {
        var response = new VoteDefaultResponse( );
        response.id                = vote.getId( );
        response.stock             = StockDefaultResponse.fromStock(vote.getStock( ));
        response.description       = vote.getDescription( );
        response.startAt           = vote.getStartAt( );
        response.endedAt           = vote.getEndedAt( );
        response.createdAt         = vote.getCreatedAt( );
        response.updatedAt        = vote.getUpdatedAt( );
        response.participantCount = vote.getParticipantsCount( );
        response.selectionCount   = vote.getSelectionCount( );
        response.ballots          = vote.getBallots( ).stream( )
                                         .map(BallotDefaultResponse::fromBallot)
                                         .collect(Collectors.toList( ));
        return response;
    }
}
