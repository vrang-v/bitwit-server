package com.server.bitwit.module.ballot;

import com.server.bitwit.module.domain.Ballot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface BallotRepository extends JpaRepository<Ballot, Long> { }
