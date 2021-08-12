package com.server.bitwit.infra.schedule;

import com.server.bitwit.domain.Vote;
import com.server.bitwit.module.stock.StockRepository;
import com.server.bitwit.module.vote.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class VoteCreationScheduler {
    
    private final StockRepository stockRepository;
    
    private final VoteRepository voteRepository;
    
    @Scheduled(cron = "0 0 0 * * *")
    public void createDailyVotes( ) {
        var today    = LocalDateTime.now( );
        var tomorrow = today.plusDays(1);
        stockRepository.findAll( )
                       .stream( )
                       .map(stock -> Vote.createVote(stock, "", today, tomorrow))
                       .forEach(voteRepository::save);
    }
}
