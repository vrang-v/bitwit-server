package com.server.bitwit.module.ballot;

import com.server.bitwit.domain.Ballot;

import java.util.Optional;

public interface BallotQueryRepository {
    Optional<Ballot> findByVoteIdAndAccountId(Long voteId, Long accountId);
}
