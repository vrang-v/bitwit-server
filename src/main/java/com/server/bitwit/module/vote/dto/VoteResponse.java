package com.server.bitwit.module.vote.dto;

import com.server.bitwit.module.ballot.dto.BallotResponse;
import com.server.bitwit.module.domain.Stock;
import com.server.bitwit.module.domain.Vote;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class VoteResponse
{
    Long id;
    
    Stock stock;
    
    List<BallotResponse> ballots;
    
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
        response.description = vote.getDescription( );
        response.startAt     = vote.getStartAt( );
        response.endedAt     = vote.getEndedAt( );
        response.createdAt   = vote.getCreatedAt( );
        response.updatedAt   = vote.getUpdatedAt( );
        response.ballots     = vote.getBallots( ).stream( )
                                   .map(BallotResponse::fromBallot)
                                   .collect(Collectors.toList( ));
        return response;
    }
}
