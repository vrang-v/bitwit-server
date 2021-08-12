package com.server.bitwit.module.vote;

import com.server.bitwit.domain.Stock;
import com.server.bitwit.domain.Vote;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface VoteRepository extends JpaRepository<Vote, Long>, VoteQueryRepository
{
    @EntityGraph(attributePaths = "stock")
    Optional<Vote> findWithStockById(Long voteId);
    
    @Query("select v from Vote v join fetch v.stock")
    List<Vote> findAllWithStock( );
    
    @Query("select v from Vote v join fetch v.stock join fetch v.ballots")
    List<Vote> findAllWithAll( );
    
    List<Vote> findAllByStock(Stock stock);
}
