package com.server.bitwit.module.vote.dto;

import com.server.bitwit.module.ballot.dto.BallotDefaultResponse;
import com.server.bitwit.module.domain.Stock;
import com.server.bitwit.module.domain.Vote;
import com.server.bitwit.module.domain.VotingOption;
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
    
    Stock stock;
    
    List<BallotDefaultResponse> ballots;
    
    String description;
    
    LocalDateTime startAt;
    
    LocalDateTime endedAt;
    
    Instant createdAt;
    
    Instant updatedAt;
    
    int participantsCount;
    
    Map<VotingOption, Integer> selectionsCount;
    
    public static VoteResponse fromVote(Vote vote)
    {
        var response = new VoteDefaultResponse( );
        response.id                = vote.getId( );
        response.stock             = vote.getStock( );
        response.description       = vote.getDescription( );
        response.startAt           = vote.getStartAt( );
        response.endedAt           = vote.getEndedAt( );
        response.createdAt         = vote.getCreatedAt( );
        response.updatedAt         = vote.getUpdatedAt( );
        response.participantsCount = vote.getParticipantsCount( );
        response.selectionsCount   = vote.getSelectionsCount( );
        response.ballots           = vote.getBallots( ).stream( )
                                         .map(BallotDefaultResponse::fromBallot)
                                         .collect(Collectors.toList( ));
        return response;
    }
}
