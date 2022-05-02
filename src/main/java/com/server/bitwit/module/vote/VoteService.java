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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.server.bitwit.util.PageUtils.getSort;
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
        var participated    = voteRepository.searchAllParticipated(cond).stream( );
        var notParticipated = voteRepository.searchAllNotParticipated(cond).stream( ).map(Vote::hideBallots);
        return Stream.concat(participated, notParticipated)
                     .map(vote -> conversionService.convert(vote, responseType))
                     .findFirst( )
                     .orElseThrow(( ) -> new NonExistentResourceException("vote"));
    }
    
    public <T> List<T> searchVotes(VoteSearchCond cond, Class<T> responseType) {
        var participated    = voteRepository.searchAllParticipated(cond).stream( );
        var notParticipated = voteRepository.searchAllNotParticipated(cond).stream( ).map(Vote::hideBallots);
        return Stream.concat(participated, notParticipated)
                     .map(vote -> conversionService.convert(vote, responseType))
                     .collect(Collectors.toList( ));
    }
    
    public <T> Page<T> searchActiveVotes(VoteSearchCond cond, Class<T> responseType, Pageable pageable) {
        var currentTime  = LocalDateTime.now( );
        var votes        = voteRepository.searchActiveVotePage(cond, pageable, currentTime);
        var participated = voteRepository.searchAllActiveVotesParticipated(cond, currentTime);
        return votes.map(vote -> {
            if (! participated.contains(vote)) {
                vote.hideBallots( );
            }
            return conversionService.convert(vote, responseType);
        });
    }
    
    public <T> List<T> searchActiveVotesWithOffset(VoteSearchCond cond, String sort, int offset, int limit, Class<T> responseType) {
        var currentTime  = LocalDateTime.now( );
        var votes        = voteRepository.searchActiveVoteWithOffset(cond, getSort(sort), offset, limit, currentTime);
        var participated = voteRepository.searchAllActiveVotesParticipated(cond, currentTime);
        return votes.stream( )
                    .map(vote -> {
                        if (! participated.contains(vote)) {
                            vote.hideBallots( );
                        }
                        return conversionService.convert(vote, responseType);
                    })
                    .collect(toList( ));
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
