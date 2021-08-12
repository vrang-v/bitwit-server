package com.server.bitwit.module.ballot;

import com.server.bitwit.domain.Ballot;

import java.util.Optional;

public interface BallotQueryRepository
{
    Optional<Ballot> findByVoteAndAccountId(Long accountId, Long voteId);
}
