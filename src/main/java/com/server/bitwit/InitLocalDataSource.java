package com.server.bitwit;

import com.server.bitwit.domain.Account;
import com.server.bitwit.domain.Post;
import com.server.bitwit.domain.Stock;
import com.server.bitwit.infra.schedule.VoteCreationScheduler;
import com.server.bitwit.module.account.AccountRepository;
import com.server.bitwit.module.post.repository.PostRepository;
import com.server.bitwit.module.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@RequiredArgsConstructor
@Component
@Profile({"local"})
public class InitLocalDataSource {
    
    private final AccountRepository accountRepository;
    private final StockRepository   stockRepository;
    private final PostRepository    postRepository;
    
    private final VoteCreationScheduler voteCreationScheduler;
    
    @EventListener(ApplicationReadyEvent.class)
    public void init( ) throws IOException {
        
        log.info("Init Local Data Source");
        
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
        
        var account1 = Account.createEmailAccount("vrang", "mingi06171@naver.com", "1234qwer").convertToVerified( );
        var account2 = Account.createEmailAccount("민트초코", "tlsalsrl0617@naver.com", "1234qwer").convertToVerified( );
        var account3 = Account.createEmailAccount("초코찐빵", "mingi000617@naver.com", "1234qwer").convertToVerified( );
        accountRepository.save(account1);
        accountRepository.save(account2);
        accountRepository.save(account3);
        
        for (int i = 0; i < 100; i++) {
            var btc   = stockRepository.findByTicker("BTC").orElseThrow( );
            var eth   = stockRepository.findByTicker("ETH").orElseThrow( );
            var post1 = new Post("안녕하세요!!! " + i, "Hello", account1).addStock(btc).addStock(eth);
            var post2 = new Post("Apple September event: iPhone 14, Apple Watch Series 8, iOS 16 release date, and more" + i, "Hello", account1).addStock(btc);
            var post3 = new Post("분기매출 2조 돌파한 네이버…\"웹툰 수익화 이제 시작\"" + i, "Hello", account1).addStock(eth);
            postRepository.save(post1);
            postRepository.save(post2);
            postRepository.save(post3);
        }
        voteCreationScheduler.createDailyVotes( );
    }
}
