package com.server.bitwit.module.vote;

import com.server.bitwit.domain.Vote;
import com.server.bitwit.module.vote.search.VoteSearchCond;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoteQueryRepository {
    
    Optional<Vote> findWithBallotWithAccountById(Long voteId);
    
    List<Vote> findAllByAccountIdAndParticipationDate(Long accountId, LocalDate date);
    
    List<Vote> findActiveVotes( );
    
    List<Vote> findAllParticipated(VoteSearchCond cond);
    
    List<Vote> findAllNotParticipated(VoteSearchCond cond);
    
}
