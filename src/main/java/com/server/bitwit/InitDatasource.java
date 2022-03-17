package com.server.bitwit;

import com.server.bitwit.domain.Account;
import com.server.bitwit.domain.Post;
import com.server.bitwit.domain.Stock;
import com.server.bitwit.infra.schedule.VoteCreationScheduler;
import com.server.bitwit.module.account.AccountRepository;
import com.server.bitwit.module.account.AccountService;
import com.server.bitwit.module.post.PostRepository;
import com.server.bitwit.module.stock.StockRepository;
import com.server.bitwit.module.vote.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@RequiredArgsConstructor
@Component
public class InitDatasource {
    
    private final AccountRepository accountRepository;
    private final StockRepository   stockRepository;
    private final PostRepository    postRepository;
    
    private final VoteCreationScheduler voteCreationScheduler;
    
    @PostConstruct
    public void init( ) throws IOException {
        
        log.info("Init Data Source");
        
        if (stockRepository.count( ) == 0) {
            var bithumbData    = new ClassPathResource("bithumb_data.csv");
            var bufferedReader = new BufferedReader(new InputStreamReader(bithumbData.getInputStream( ), UTF_8));
            var tickers        = new ArrayList<String>( );
            while (bufferedReader.ready( )) {
                tickers.add(bufferedReader.readLine( ));
            }
            tickers.stream( )
                   .map(line -> line.replace("\uFEFF", ""))
                   .map(line -> line.split(","))
                   .map(split -> Stock.createStock(split[0].trim( ), "", split[1].trim( )))
                   .forEach(stockRepository::save);
        }
        
        var account1 = Account.createEmailAccount("vrang", "mingi06171@naver.com", "1234qwer").verify( );
        var account2 = Account.createEmailAccount("민트초코", "tlsalsrl0617@naver.com", "1234qwer").verify( );
        var account3 = Account.createEmailAccount("초코찐빵", "mingi000617@naver.com", "1234qwer").verify( );
        accountRepository.save(account1);
        accountRepository.save(account2);
        accountRepository.save(account3);
        
        for (int i = 0; i < 100; i++) {
            var btc   = stockRepository.findByTicker("BTC").orElseThrow( );
            var eth   = stockRepository.findByTicker("ETH").orElseThrow( );
            var post1 = new Post("비트코인 떡상" + i, "Hello", account1).addStock(btc).addStock(eth);
            var post2 = new Post("비트코인 떡락" + i, "Hello", account1).addStock(btc);
            var post3 = new Post("이더리움 떡상" + i, "Hello", account1).addStock(eth);
            postRepository.save(post1);
            postRepository.save(post2);
            postRepository.save(post3);
        }
        voteCreationScheduler.createDailyVotes( );
    }
}
