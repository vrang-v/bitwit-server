package com.server.bitwit.infra.schedule;

import com.server.bitwit.infra.client.bithumb.BithumbService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Profile({"!test & !debug"})
public class BithumbScheduler {
    
    private final BithumbService bithumbService;
    
    @Scheduled(fixedDelay = 3000)
    public void updateRealTimePriceTask( ) {
        bithumbService.updateAllRealTimePrice( );
    }
    
}
