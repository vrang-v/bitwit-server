package com.server.bitwit;

import com.server.bitwit.domain.Stock;
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
@Profile({"!local"})
public class InitDataSource {
    
    private final StockRepository   stockRepository;
    
    @EventListener(ApplicationReadyEvent.class)
    public void init( ) throws IOException {
        
        log.info("Init Stock");
        
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
    }
}
