package com.server.bitwit.module.stock;

import com.server.bitwit.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface StockRepository extends JpaRepository<Stock, Long> {
    
    Optional<Stock> findByTicker(String ticker);
    
}
