package com.server.bitwit.module.vote;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.bitwit.domain.QVote;
import com.server.bitwit.domain.Vote;
import com.server.bitwit.module.vote.search.VoteSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static com.querydsl.jpa.JPAExpressions.selectFrom;
import static com.server.bitwit.domain.QAccount.account;
import static com.server.bitwit.domain.QBallot.ballot;
import static com.server.bitwit.domain.QVote.vote;
import static com.server.bitwit.util.DynamicQueryUtils.combineWithOr;
import static com.server.bitwit.util.DynamicQueryUtils.in;

@RequiredArgsConstructor
@Repository
public class VoteQueryRepositoryImpl implements VoteQueryRepository {
    
    private final JPAQueryFactory query;
    
    @Override
    public Optional<Vote> findWithBallotWithAccountById(Long voteId) {
        return Optional.ofNullable(
                query.selectFrom(vote)
                     .leftJoin(ballot.account, account).fetchJoin( )
                     .leftJoin(vote.ballots, ballot)
                     .where(vote.id.eq(voteId))
                     .fetchOne( )
        );
    }
    
    @Override
    public List<Vote> findActiveVotes( ) {
        var now = LocalDateTime.now( );
        return query.selectFrom(vote)
                    .leftJoin(vote.stock).fetchJoin( )
                    .where(
                            vote.startAt.before(now),
                            vote.endedAt.after(now)
                    )
                    .fetch( );
    }
    
    @Override
    public List<Vote> findAllParticipated(VoteSearchCond cond) {
        return query
                .selectFrom(vote)
                .distinct( )
                .innerJoin(vote.stock).fetchJoin( )
                .innerJoin(vote.ballots, ballot)
                .where(
                        ballot.account.id.eq(cond.getAccountId( )),
                        combineWithOr(
                                in(vote.id, cond.getVoteIds( )),
                                in(vote.stock.ticker, cond.getTickers( )),
                                in(vote.stock.id, cond.getStockIds( ))
                        )
                )
                .orderBy(vote.endedAt.desc( ))
                .fetch( );
    }
    
    @Override
    public List<Vote> findAllNotParticipated(VoteSearchCond cond) {
        var _vote = new QVote("sub");
        var participatedVotes =
                selectFrom(_vote)
                        .innerJoin(_vote.ballots, ballot)
                        .where(ballot.account.id.eq(cond.getAccountId( )));
        return query
                .selectFrom(vote)
                .innerJoin(vote.stock).fetchJoin( )
                .where(
                        vote.notIn(participatedVotes),
                        combineWithOr(
                                in(vote.id, cond.getVoteIds( )),
                                in(vote.stock.ticker, cond.getTickers( )),
                                in(vote.stock.id, cond.getStockIds( ))
                        )
                )
                .orderBy(vote.endedAt.desc( ))
                .fetch( );
    }
    
    @Override
    public List<Vote> findAllByAccountIdAndParticipationDate(Long accountId, LocalDate date) {
        var from = date.atStartOfDay( ).toInstant(ZoneOffset.UTC);
        var to   = from.plus(1, ChronoUnit.DAYS);
        return query.selectFrom(vote)
                    .leftJoin(vote.stock).fetchJoin( )
                    .leftJoin(vote.ballots, ballot)
                    .leftJoin(ballot.account, account)
                    .where(
                            account.id.eq(accountId),
                            ballot.createdAt.between(from, to)
                    )
                    .fetch( );
    }
}
