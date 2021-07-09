package com.server.bitwit.module.vote;

import com.server.bitwit.infra.exception.NotFoundException;
import com.server.bitwit.module.domain.Vote;
import com.server.bitwit.module.stock.StockService;
import com.server.bitwit.module.vote.dto.VoteRequest;
import com.server.bitwit.module.vote.dto.VoteResponse;
import com.server.bitwit.module.vote.dto.VoteResponseType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class VoteService
{
    private final VoteRepository voteRepository;
    
    private final StockService stockService;
    
    @Transactional
    public Long createVote(VoteRequest request)
    {
        var stock = stockService.getStock(request.getStockId( )).orElseThrow( );
        return voteRepository.save(request.toVote(stock)).getId( );
    }
    
    public Optional<Vote> getVote(Long voteId)
    {
        return voteRepository.findById(voteId);
    }
    
    public VoteResponse getVoteResponse(Long voteId)
    {
        return getVoteResponse(voteId, VoteResponseType.DEFAULT);
    }
    
    public VoteResponse getVoteResponse(Long voteId, VoteResponseType responseType)
    {
        return getVote(voteId)
                .map(responseType::fromVote)
                .orElseThrow(NotFoundException::new);
    }
    
    public List<Vote> getAllVotes( )
    {
        return voteRepository.findAll( );
    }
    
    public List<VoteResponse> getAllVoteResponses( )
    {
        return getAllVoteResponses(VoteResponseType.DEFAULT);
    }
    
    public List<VoteResponse> getAllVoteResponses(VoteResponseType responseType)
    {
        return getAllVotes( ).stream( )
                             .map(responseType::fromVote)
                             .collect(toList( ));
    }
    
    public boolean existsById(Long voteId)
    {
        return voteRepository.existsById(voteId);
    }
}
