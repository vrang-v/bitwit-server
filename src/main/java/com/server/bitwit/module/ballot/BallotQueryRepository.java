package com.server.bitwit.module.ballot;

import com.server.bitwit.domain.Ballot;
import com.server.bitwit.module.ballot.search.BallotSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BallotQueryRepository {
    
    List<Ballot> searchBallot(BallotSearchCondition condition);
    
    Page<Ballot> searchBallot(BallotSearchCondition condition, Pageable pageable);
    
    Optional<Ballot> findByVoteIdAndAccountId(Long voteId, Long accountId);
    
}
