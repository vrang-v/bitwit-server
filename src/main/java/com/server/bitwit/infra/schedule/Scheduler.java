package com.server.bitwit.infra.schedule;

import com.server.bitwit.infra.client.bithumb.BithumbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {
    
    private final BithumbService bithumbService;
    
    @Scheduled(initialDelay = 5000, fixedDelay = 3000)
    public void updateRealTimePriceTask( ) {
        bithumbService.updateRealTimePrice( );
    }
    
    @Scheduled(fixedDelay = 86400000)
    public void updateBithumb24hCandlestickChart( ) {
        bithumbService.get24hCandleStickChart( );
    }
}
