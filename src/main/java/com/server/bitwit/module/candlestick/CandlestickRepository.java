package com.server.bitwit.module.candlestick;

import com.server.bitwit.domain.Candlestick;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandlestickRepository extends JpaRepository<Candlestick, Long> {
    
    List<Candlestick> findAllByTicker(String ticker, Pageable pageable);
    
}
