package com.server.bitwit.module.ballot;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.bitwit.domain.Ballot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.server.bitwit.domain.QAccount.account;
import static com.server.bitwit.domain.QBallot.ballot;
import static com.server.bitwit.domain.QVote.vote;

@RequiredArgsConstructor
@Repository
public class BallotQueryRepositoryImpl implements BallotQueryRepository
{
    private final JPAQueryFactory query;
    
    @Override
    public Optional<Ballot> findByVoteAndAccountId(Long accountId, Long voteId)
    {
        return Optional.ofNullable(
                query.selectFrom(ballot)
                     .join(ballot.vote, vote)
                     .join(ballot.account, account)
                     .where(vote.id.eq(voteId),
                             account.id.eq(accountId)
                     )
                     .fetchOne( )
        );
    }
}
