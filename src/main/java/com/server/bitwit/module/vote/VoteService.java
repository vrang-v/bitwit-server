package com.server.bitwit.module.vote;

import com.server.bitwit.domain.Vote;
import com.server.bitwit.module.error.exception.NonExistentResourceException;
import com.server.bitwit.module.stock.StockService;
import com.server.bitwit.module.vote.dto.CreateVoteRequest;
import com.server.bitwit.module.vote.dto.VoteClientResponse;
import com.server.bitwit.module.vote.dto.VoteItemResponse;
import com.server.bitwit.module.vote.search.VoteSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class VoteService {
    
    private final VoteRepository voteRepository;
    
    private final StockService      stockService;
    private final ConversionService conversionService;
    
    @Transactional
    public VoteItemResponse createVote(CreateVoteRequest request) {
        return Optional.ofNullable(conversionService.convert(request, Vote.class))
                       .map(voteRepository::save)
                       .map(vote -> conversionService.convert(vote, VoteItemResponse.class))
                       .orElseThrow( );
    }
    
    public List<VoteClientResponse> getVotesByAccountIdAndParticipationDate(Long accountId, LocalDate date) {
        return voteRepository.findAllByAccountIdAndParticipationDate(accountId, date)
                             .stream( )
                             .map(vote -> conversionService.convert(vote, VoteClientResponse.class))
                             .collect(Collectors.toList( ));
    }
    
    public List<VoteClientResponse> getActiveVotes( ) {
        return voteRepository.findActiveVotes( )
                             .stream( )
                             .map(vote -> conversionService.convert(vote, VoteClientResponse.class))
                             .collect(toList( ));
    }
    
    
    public <T> T searchVote(VoteSearchCond cond, Class<T> responseType) {
        var participated    = voteRepository.findAllParticipated(cond).stream( );
        var notParticipated = voteRepository.findAllNotParticipated(cond).stream( ).map(Vote::hideBallots);
        return Stream.concat(participated, notParticipated)
                     .map(vote -> conversionService.convert(vote, responseType))
                     .findFirst( )
                     .orElseThrow(( ) -> new NonExistentResourceException("vote"));
    }
    
    public <T> List<T> searchVotes(VoteSearchCond cond, Class<T> responseType) {
        var participated    = voteRepository.findAllParticipated(cond).stream( );
        var notParticipated = voteRepository.findAllNotParticipated(cond).stream( ).map(Vote::hideBallots);
        return Stream.concat(participated, notParticipated)
                     .map(vote -> conversionService.convert(vote, responseType))
                     .collect(Collectors.toList( ));
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
