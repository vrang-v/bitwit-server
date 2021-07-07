package com.server.bitwit.module.stock;

import com.server.bitwit.module.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StockRepository extends JpaRepository<Stock, Long> { }
