package com.server.bitwit.module.vote.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.bitwit.domain.Ballot;
import com.server.bitwit.domain.Vote;
import com.server.bitwit.domain.VotingOption;
import com.server.bitwit.infra.config.MapStructConfig;
import com.server.bitwit.module.ballot.dto.BallotResponse;
import com.server.bitwit.module.ballot.dto.BallotResponse.BallotResponseMapper;
import com.server.bitwit.module.stock.dto.StockResponse;
import lombok.Data;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.CollectionUtils;

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
    
    @Mapper(config = MapStructConfig.class, uses = BallotResponseMapper.class)
    public interface VoteItemResponseMapper extends Converter<Vote, VoteItemResponse> {
        
        @Override
        @Mapping(target = "ballot", source = "vote")
        VoteItemResponse convert(Vote vote);
        
        default Ballot ballot(Vote vote) {
            return CollectionUtils.isEmpty(vote.getBallots( )) ? null : vote.getBallots( ).get(0);
        }
        
        @AfterMapping
        default void afterMapping(@MappingTarget VoteItemResponse response) {
            if (response.getBallot( ) == null) {
                response.setSelectionCount(null);
            }
        }
    }
}
