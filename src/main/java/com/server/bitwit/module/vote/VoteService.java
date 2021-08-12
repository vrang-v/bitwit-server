package com.server.bitwit.module.vote;

import com.server.bitwit.module.error.exception.NotFoundException;
import com.server.bitwit.domain.Vote;
import com.server.bitwit.module.stock.StockService;
import com.server.bitwit.module.vote.dto.*;
import com.server.bitwit.module.vote.search.VoteParticipationSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class VoteService {
    private final VoteRepository voteRepository;
    
    private final StockService stockService;
    
    @Transactional
    public Long createVote(VoteRequest request) {
        var stock = stockService.findById(request.getStockId( )).orElseThrow( );
        return voteRepository.save(request.toVote(stock)).getId( );
    }
    
    public VoteResponse getVoteResponse(Long voteId) {
        return getVoteResponse(voteId, VoteResponseType.DEFAULT);
    }
    
    public VoteResponse getVoteResponse(Long voteId, VoteResponseType responseType) {
        return voteRepository
                .findWithStockById(voteId)
                .map(responseType::fromVote)
                .orElseThrow(NotFoundException::new);
    }
    
    public List<VoteResponse> getAllVoteResponses( ) {
        return getAllVoteResponses(VoteResponseType.DEFAULT);
    }
    
    public List<VoteResponse> getAllVoteResponses(VoteResponseType responseType) {
        return voteRepository
                .findAllWithAll( )
                .stream( )
                .map(responseType::fromVote)
                .collect(toList( ));
    }
    
    public List<VoteClientResponse> getVotesByAccountIdAndParticipationDate(Long accountId, LocalDate date) {
        return voteRepository
                .findAllByAccountIdAndParticipationDate(accountId, date)
                .stream( )
                .map(VoteClientResponse::fromVote)
                .collect(Collectors.toList( ));
    }
    
    public List<VoteClientResponse> getVotesByStockId(Long stockId) {
        var stock = stockService.findById(stockId).orElseThrow( );
        return voteRepository
                .findAllByStock(stock)
                .stream( )
                .map(VoteClientResponse::fromVote)
                .collect(Collectors.toList( ));
    }
    
    public List<VoteClientResponse> getActiveVotes( ) {
        return voteRepository
                .findActiveVotes( )
                .stream( )
                .map(VoteClientResponse::fromVote)
                .collect(toList( ));
    }
    
    public VoteItemResponse getVoteItem(Long voteId, Long accountId) {
        var votes = voteRepository.findAllByParticipation(new VoteParticipationSearch(voteId, accountId, true));
        if (! votes.isEmpty( )) {
            return VoteItemResponse.fromParticipating(votes.get(0));
        }
        votes = voteRepository.findAllByParticipation(new VoteParticipationSearch(voteId, accountId, false));
        return VoteItemResponse.fromNotParticipating(votes.get(0));
    }
    
    public List<VoteResponse> getVoteItems(Long accountId) {
        var voteResponses = new ArrayList<VoteResponse>( );
        voteRepository.findAllByParticipation(new VoteParticipationSearch(null, accountId, true))
                      .stream( )
                      .map(VoteItemResponse::fromParticipating)
                      .forEach(voteResponses::add);
        voteRepository.findAllByParticipation(new VoteParticipationSearch(null, accountId, false))
                      .stream( )
                      .map(VoteItemResponse::fromNotParticipating)
                      .forEach(voteResponses::add);
        return voteResponses;
    }
    
    public Optional<Vote> findById(Long voteId) {
        return voteRepository.findWithStockById(voteId);
    }
    
    public List<Vote> findAll( ) {
        return voteRepository.findAll( );
    }
    
    public Optional<Vote> findVoteWithAll(Long voteId) {
        return voteRepository.findWithBallotWithAccountById(voteId);
    }
    
    public boolean existsById(Long voteId) {
        return voteRepository.existsById(voteId);
    }
}
