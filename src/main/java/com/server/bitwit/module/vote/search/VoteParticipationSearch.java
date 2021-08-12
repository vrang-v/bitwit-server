package com.server.bitwit.module.vote.search;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class VoteParticipationSearch {
    Long voteId;
    
    @NotNull
    Long accountId;
    
    @NotEmpty
    boolean participation;
}
