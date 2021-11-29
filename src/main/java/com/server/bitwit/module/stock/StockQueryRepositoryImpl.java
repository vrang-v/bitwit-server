package com.server.bitwit.module.stock;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.bitwit.domain.Stock;
import com.server.bitwit.module.stock.dto.SearchStockCond;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.server.bitwit.domain.QStock.stock;
import static com.server.bitwit.util.DynamicQueryUtils.combineWithOr;
import static com.server.bitwit.util.DynamicQueryUtils.in;
import static com.server.bitwit.util.DynamicQueryUtils.startsWithIgnoreCase;

@RequiredArgsConstructor
@Repository
public class StockQueryRepositoryImpl implements StockQueryRepository {
    
    private final JPAQueryFactory query;
    
    @Override
    public List<Stock> searchStocks(SearchStockCond cond) {
        return query
                .selectFrom(stock)
                .where(
                        combineWithOr(
                                startsWithIgnoreCase(stock.ticker, cond.getKeyword( )),
                                startsWithIgnoreCase(stock.fullName, cond.getKeyword( )),
                                startsWithIgnoreCase(stock.koreanName, cond.getKeyword( )),
                                in(stock.ticker, cond.getTickers( ))
                        )
                )
                .fetch( );
    }
}
