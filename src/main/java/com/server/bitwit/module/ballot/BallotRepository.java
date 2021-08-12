package com.server.bitwit.module.ballot;

import com.server.bitwit.domain.Ballot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BallotRepository extends JpaRepository<Ballot, Long>, BallotQueryRepository { }
