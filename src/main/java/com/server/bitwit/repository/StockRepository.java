package com.server.bitwit.repository;

import com.server.bitwit.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StockRepository extends JpaRepository<Stock, Long> { }
