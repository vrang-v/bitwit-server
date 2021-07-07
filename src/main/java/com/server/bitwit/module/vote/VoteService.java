package com.server.bitwit.module.vote;

import com.server.bitwit.module.vote.dto.VoteRequest;
import com.server.bitwit.module.vote.dto.VoteResponse;
import com.server.bitwit.infra.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class VoteService
{
    private final VoteRepository voteRepository;
    
    @Transactional
    public Long createVote(VoteRequest request)
    {
        return voteRepository.save(request.toVote( )).getId( );
    }
    
    public VoteResponse getVote(Long voteId)
    {
        return voteRepository.findById(voteId)
                             .map(VoteResponse::fromVote)
                             .orElseThrow(NotFoundException::new);
    }
    
    public List<VoteResponse> getAllVotes( )
    {
        return voteRepository.findAll( ).stream( )
                             .map(VoteResponse::fromVote)
                             .collect(Collectors.toList( ));
    }
    
    public boolean existsById(Long voteId)
    {
        return voteRepository.existsById(voteId);
    }
}
