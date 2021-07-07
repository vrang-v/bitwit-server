package com.server.bitwit.repository;

import com.server.bitwit.domain.Ballot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface BallotRepository extends JpaRepository<Ballot, Long> { }
