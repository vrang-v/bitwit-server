package com.server.bitwit.module.ballot;

import com.server.bitwit.domain.Ballot;
import com.server.bitwit.module.ballot.search.BallotSearchCondition;
import com.server.bitwit.util.QuerydslRepositoryBase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.server.bitwit.domain.QAccount.account;
import static com.server.bitwit.domain.QBallot.ballot;
import static com.server.bitwit.domain.QStock.stock;
import static com.server.bitwit.domain.QVote.vote;
import static com.server.bitwit.util.DynamicQueryUtils.between;
import static com.server.bitwit.util.DynamicQueryUtils.eq;

@Repository
public class BallotQueryRepositoryImpl extends QuerydslRepositoryBase implements BallotQueryRepository {
    
    protected BallotQueryRepositoryImpl( ) {
        super(Ballot.class);
    }
    
    @Override
    public List<Ballot> searchBallot(BallotSearchCondition condition) {
        return selectFrom(ballot)
                .leftJoin(ballot.account, account)
                .leftJoin(ballot.vote, vote)
                .leftJoin(vote.stock, stock)
                .where(
                        eq(account.id, condition.getAccountId( )),
                        eq(vote.id, condition.getVoteId( )),
                        eq(stock.id, condition.getStockId( )),
                        eq(stock.ticker, condition.getTicker( )),
                        between(ballot.createdAt, condition.getBefore( ), condition.getAfter( ))
                )
                .fetch( );
    }
    
    @Override
    public Page<Ballot> searchBallot(BallotSearchCondition condition, Pageable pageable) {
        return paginate(pageable,
                selectFrom(ballot)
                        .leftJoin(ballot.account, account)
                        .leftJoin(ballot.vote, vote)
                        .leftJoin(vote.stock, stock)
                        .where(
                                eq(account.id, condition.getAccountId( )),
                                eq(vote.id, condition.getVoteId( )),
                                eq(stock.id, condition.getStockId( )),
                                eq(stock.ticker, condition.getTicker( )),
                                between(ballot.createdAt, condition.getBefore( ), condition.getAfter( ))
                        )
        );
    }
    
    @Override
    public Optional<Ballot> findByVoteIdAndAccountId(Long voteId, Long accountId) {
        return Optional.ofNullable(
                selectFrom(ballot)
                        .innerJoin(ballot.vote, vote)
                        .innerJoin(ballot.account, account)
                        .where(
                                vote.id.eq(voteId),
                                account.id.eq(accountId)
                        )
                        .fetchOne( )
        );
    }
}
