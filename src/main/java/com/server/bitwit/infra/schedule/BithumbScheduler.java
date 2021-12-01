package com.server.bitwit.infra.schedule;

import com.server.bitwit.infra.client.bithumb.BithumbService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BithumbScheduler {
    
    private final BithumbService bithumbService;
    
    @Scheduled(fixedDelay = 3000)
    public void updateRealTimePriceTask( ) {
        bithumbService.updateRealTimePrice( );
    }
    
    //@Scheduled(fixedDelay = 86400000)
    public void updateBithumb24hCandlestickChart( ) {
        bithumbService.get24hCandleStickChart( );
    }
}
