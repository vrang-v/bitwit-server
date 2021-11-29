package com.server.bitwit;

import com.server.bitwit.domain.Account;
import com.server.bitwit.domain.Post;
import com.server.bitwit.domain.Stock;
import com.server.bitwit.domain.Vote;
import com.server.bitwit.infra.schedule.VoteCreationScheduler;
import com.server.bitwit.module.account.AccountRepository;
import com.server.bitwit.module.account.AccountService;
import com.server.bitwit.module.post.PostRepository;
import com.server.bitwit.module.stock.StockRepository;
import com.server.bitwit.module.vote.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
@Profile("!test")
public class InitDatasource {
    
    private final AccountRepository accountRepository;
    private final VoteRepository    voteRepository;
    private final StockRepository   stockRepository;
    private final PostRepository    postRepository;
    
    private final AccountService accountService;
    
    private final VoteCreationScheduler voteCreationScheduler;
    
    @PostConstruct
    public void init( ) throws IOException {
        
        log.info("Init Data Source");
        
        if (stockRepository.count( ) == 0) {
            var bithumbData = new ClassPathResource("bithumb_data.csv");
            Files.readAllLines(bithumbData.getFile( ).toPath( ), StandardCharsets.UTF_8)
                 .stream( )
                 .map(line -> line.replaceAll("\uFEFF", ""))
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
    
    //    @PostConstruct
    public void initDatasource( ) {
        var apple     = Stock.createStock("AAPL", "Apple", "애플");
        var amazon    = Stock.createStock("AMZN", "Amazon.com", "아마존닷컴");
        var microsoft = Stock.createStock("MSFT", "Microsoft", "마이크로소프트");
        var netflix   = Stock.createStock("NFLX", "Netflix", "넷플릭스");
        var google    = Stock.createStock("GOOG", "Google", "구글");
        var bitcoin   = Stock.createStock("BTC", "Bitcoin", "비트코인");
        var ethereum  = Stock.createStock("ETH", "Ethereum", "이더리움");
        
        stockRepository.save(apple);
        stockRepository.save(amazon);
        stockRepository.save(microsoft);
        stockRepository.save(netflix);
        stockRepository.save(google);
        stockRepository.save(bitcoin);
        stockRepository.save(ethereum);
        
        var vote1 = Vote.createVote(bitcoin, "Hello", LocalDateTime.now( ), LocalDateTime.of(2021, 9, 27, 8, 10));
        var vote2 = Vote.createVote(bitcoin, "BTC", LocalDateTime.now( ), LocalDateTime.of(2021, 9, 9, 8, 10));
        var vote3 = Vote.createVote(bitcoin, "BTC", LocalDateTime.now( ), LocalDateTime.of(2021, 9, 9, 8, 10));
        var vote4 = Vote.createVote(bitcoin, "BTC", LocalDateTime.now( ), LocalDateTime.of(2021, 9, 9, 8, 10));
        var vote5 = Vote.createVote(bitcoin, "BTC", LocalDateTime.now( ), LocalDateTime.of(2021, 9, 9, 8, 10));
        var vote6 = Vote.createVote(bitcoin, "BTC", LocalDateTime.now( ), LocalDateTime.of(2021, 9, 9, 8, 10));
        var vote7 = Vote.createVote(bitcoin, "BTC", LocalDateTime.now( ), LocalDateTime.of(2021, 9, 9, 8, 10));
        var vote8 = Vote.createVote(bitcoin, "BTC", LocalDateTime.now( ), LocalDateTime.of(2021, 9, 9, 8, 10));
        
        voteRepository.save(vote1);
        voteRepository.save(vote2);
        voteRepository.save(vote3);
        voteRepository.save(vote4);
        voteRepository.save(vote5);
        voteRepository.save(vote6);
        voteRepository.save(vote7);
        voteRepository.save(vote8);
        
        for (var i = 0; i < 20; i++) {
            var vote = Vote.createVote(ethereum, "Hello", LocalDateTime.now( ), LocalDateTime.of(2021, 9, 27, 8, 10));
            voteRepository.save(vote);
        }
        
        var account1 = Account.createEmailAccount("mingi", "mingi0617@naver.com", "1234qwer");
        var account2 = Account.createEmailAccount("mingi", "tlsalsrl0617@naver.com", "1234qwer");
        var account3 = Account.createEmailAccount("mingi", "mingi000617@naver.com", "1234qwer");
        accountRepository.save(account1);
        accountRepository.save(account2);
        accountRepository.save(account3);
    }
}
