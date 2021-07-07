package com.server.bitwit.module.vote;

import com.server.bitwit.module.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface VoteRepository extends JpaRepository<Vote, Long> { }
