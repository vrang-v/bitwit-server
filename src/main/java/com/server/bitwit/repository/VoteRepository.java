package com.server.bitwit.repository;

import com.server.bitwit.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface VoteRepository extends JpaRepository<Vote, Long> { }
