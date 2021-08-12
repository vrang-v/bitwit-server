package com.server.bitwit.module.ballot.dto;

import com.server.bitwit.domain.VotingOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrChangeBallotRequest
{
    @NotNull
    Long voteId;
    
    @NotNull
    VotingOption votingOption;
}
