package com.server.bitwit.module.vote.dto;

import com.server.bitwit.module.ballot.dto.BallotDefaultResponse;
import com.server.bitwit.module.ballot.dto.BallotResponse;
import com.server.bitwit.domain.Stock;
import com.server.bitwit.domain.Vote;
import com.server.bitwit.domain.VotingOption;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class VoteItemResponse implements VoteResponse {
    
    Long id;
    
    Stock stock;
    
    String description;
    
    LocalDateTime startAt;
    
    LocalDateTime endedAt;
    
    int participantCount;
    
    BallotResponse ballot;
    
    Map<VotingOption, Integer> selectionCount;
    
    public static VoteItemResponse fromParticipating(Vote vote) {
        var response = new VoteItemResponse( );
        response.id               = vote.getId( );
        response.stock            = vote.getStock( );
        response.description      = vote.getDescription( );
        response.startAt          = vote.getStartAt( );
        response.endedAt          = vote.getEndedAt( );
        response.participantCount = vote.getParticipantsCount( );
        response.selectionCount   = vote.getSelectionCount( );
        response.ballot           = BallotDefaultResponse.fromBallot(vote.getBallots( ).get(0));
        return response;
    }
    
    public static VoteItemResponse fromNotParticipating(Vote vote) {
        var response = new VoteItemResponse( );
        response.id               = vote.getId( );
        response.stock            = vote.getStock( );
        response.description      = vote.getDescription( );
        response.startAt          = vote.getStartAt( );
        response.endedAt          = vote.getEndedAt( );
        response.participantCount = vote.getParticipantsCount( );
        return response;
    }
}
