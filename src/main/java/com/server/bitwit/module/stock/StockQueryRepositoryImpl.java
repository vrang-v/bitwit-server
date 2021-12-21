package com.server.bitwit.module.stock;

import com.server.bitwit.domain.Stock;
import com.server.bitwit.module.stock.dto.SearchStockCond;
import com.server.bitwit.util.QuerydslRepositoryBase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.server.bitwit.domain.QStock.stock;
import static com.server.bitwit.util.DynamicQueryUtils.combineWithOr;
import static com.server.bitwit.util.DynamicQueryUtils.in;
import static com.server.bitwit.util.DynamicQueryUtils.startsWithIgnoreCase;

@Repository
public class StockQueryRepositoryImpl extends QuerydslRepositoryBase implements StockQueryRepository {
    
    protected StockQueryRepositoryImpl( ) {
        super(Stock.class);
    }
    
    @Override
    public List<Stock> searchStocks(SearchStockCond cond) {
        return selectFrom(stock)
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
    
    @Override
    public Page<Stock> searchStocks(SearchStockCond cond, Pageable pageable) {
        return paginate(pageable,
                selectFrom(stock)
                        .where(
                                combineWithOr(
                                        startsWithIgnoreCase(stock.ticker, cond.getKeyword( )),
                                        startsWithIgnoreCase(stock.fullName, cond.getKeyword( )),
                                        startsWithIgnoreCase(stock.koreanName, cond.getKeyword( )),
                                        in(stock.ticker, cond.getTickers( ))
                                )
                        )
        );
    }
}
