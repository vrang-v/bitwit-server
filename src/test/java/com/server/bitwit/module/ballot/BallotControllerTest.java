package com.server.bitwit.module.ballot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.bitwit.domain.VotingOption;
import com.server.bitwit.module.account.AccountRepository;
import com.server.bitwit.module.account.AccountService;
import com.server.bitwit.module.ballot.dto.CreateOrChangeBallotRequest;
import com.server.bitwit.module.error.exception.ErrorCode;
import com.server.bitwit.module.security.AccountPrincipal;
import com.server.bitwit.module.stock.StockService;
import com.server.bitwit.module.stock.dto.CreateStockRequest;
import com.server.bitwit.module.vote.VoteService;
import com.server.bitwit.module.vote.dto.CreateVoteRequest;
import com.server.bitwit.util.MockJwt;
import com.server.bitwit.util.MockMvcTest;
import com.server.bitwit.util.WithMockAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static com.server.bitwit.domain.VotingOption.DECREMENT;
import static java.time.LocalDateTime.now;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
@Transactional
class BallotControllerTest {
    
    @Autowired MockMvc      mockMvc;
    @Autowired MockJwt      mockJwt;
    @Autowired ObjectMapper objectMapper;
    
    @Autowired AccountService accountService;
    @Autowired BallotService  ballotService;
    @Autowired StockService   stockService;
    @Autowired VoteService    voteService;
    
    @Autowired AccountRepository accountRepository;
    
    @Test
    @WithMockAccount
    @DisplayName("Ballot 생성 / 정상 / 201")
    void createBallot_normal_201( ) throws Exception {
        // given
        var stockId           = stockService.createStock(new CreateStockRequest("AAPL", "Apple.inc", "애플")).getId( );
        var createVoteRequest = new CreateVoteRequest(stockId, "test", now( ), now( ).plusDays(1));
        var voteId            = voteService.createVote(createVoteRequest).getId( );
        var ballotRequest     = new CreateOrChangeBallotRequest(voteId, VotingOption.INCREMENT);
        
        // then
        mockMvc.perform(post("/api/ballots")
                       .contentType(APPLICATION_JSON)
                       .header(AUTHORIZATION, mockJwt.getBearerToken( ))
                       .content(objectMapper.writeValueAsString(ballotRequest))
               )
               .andExpectAll(
                       status( ).isCreated( ),
                       jsonPath("ballotId").exists( ),
                       jsonPath("status").value("CREATED")
               );
    }
    
    @Test
    @WithMockAccount
    @DisplayName("Ballot 생성 / 존재하지 않는 accountId / 404")
    void createBallot_nonExistentAccountId_400( ) throws Exception {
        // given
        var stockId           = stockService.createStock(new CreateStockRequest("AAPL", "Apple.inc", "애플")).getId( );
        var createVoteRequest = new CreateVoteRequest(stockId, "test", now( ), now( ).plusDays(1));
        var voteId            = voteService.createVote(createVoteRequest).getId( );
        var ballotRequest     = new CreateOrChangeBallotRequest(voteId, VotingOption.INCREMENT);
        accountRepository.deleteById(getMockAccountId( ));
        
        // then
        mockMvc.perform(post("/api/ballots")
                       .contentType(APPLICATION_JSON)
                       .header(AUTHORIZATION, mockJwt.getBearerToken( ))
                       .content(objectMapper.writeValueAsString(ballotRequest))
               )
               .andExpectAll(
                       status( ).isNotFound( ),
                       jsonPath("code").value(ErrorCode.RESOURCE_NOT_FOUND.getCode( ))
               );
    }
    
    @Test
    @WithMockAccount
    @DisplayName("Ballot 생성 / 존재하지 않는 voteId / 404")
    void createBallot_nonExistentStockId_400( ) throws Exception {
        // given
        var ballotRequest = new CreateOrChangeBallotRequest(- 1L, VotingOption.INCREMENT);
        
        // then
        mockMvc.perform(post("/api/ballots")
                       .contentType(APPLICATION_JSON)
                       .header(AUTHORIZATION, mockJwt.getBearerToken( ))
                       .content(objectMapper.writeValueAsString(ballotRequest))
               )
               .andExpectAll(
                       status( ).isNotFound( ),
                       jsonPath("code").value(ErrorCode.RESOURCE_NOT_FOUND.getCode( ))
               );
    }
    
    @Test
    @WithMockAccount
    @DisplayName("Ballot 업데이트 / 정상 / 200")
    void updateBallot_normal_200( ) throws Exception {
        // given
        var stockId           = stockService.createStock(new CreateStockRequest("AAPL", "Apple.inc", "애플")).getId( );
        var createVoteRequest = new CreateVoteRequest(stockId, "test", now( ), now( ).plusDays(1));
        var voteId            = voteService.createVote(createVoteRequest).getId( );
        var ballotRequest     = new CreateOrChangeBallotRequest(voteId, VotingOption.INCREMENT);
        var accountId         = getMockAccountId( );
        ballotService.createOrChangeBallot(accountId, ballotRequest);
        ballotRequest.setVotingOption(DECREMENT);
        
        // then
        mockMvc.perform(post("/api/ballots")
                       .contentType(APPLICATION_JSON)
                       .header(AUTHORIZATION, mockJwt.getBearerToken( ))
                       .content(objectMapper.writeValueAsString(ballotRequest))
               )
               .andExpectAll(
                       status( ).isOk( ),
                       jsonPath("ballotId").exists( ),
                       jsonPath("voteId").value(voteId),
                       jsonPath("votingOption").value("DECREMENT"),
                       jsonPath("status").value("UPDATED")
               );
    }
    
    @Test
    @WithMockAccount
    @DisplayName("Ballot 삭제 / 정상 / 200")
    void deleteBallot_normal_200( ) throws Exception {
        // given
        var stockId           = stockService.createStock(new CreateStockRequest("AAPL", "Apple.inc", "애플")).getId( );
        var createVoteRequest = new CreateVoteRequest(stockId, "test", now( ), now( ).plusDays(1));
        var voteId            = voteService.createVote(createVoteRequest).getId( );
        var ballotRequest     = new CreateOrChangeBallotRequest(voteId, VotingOption.INCREMENT);
        var accountId         = getMockAccountId( );
        ballotService.createOrChangeBallot(accountId, ballotRequest);
        
        // when, then
        mockMvc.perform(post("/api/ballots")
                       .contentType(APPLICATION_JSON)
                       .header(AUTHORIZATION, mockJwt.getBearerToken( ))
                       .content(objectMapper.writeValueAsString(ballotRequest))
               )
               .andExpectAll(
                       status( ).isOk( ),
                       jsonPath("ballotId").exists( ),
                       jsonPath("voteId").value(voteId),
                       jsonPath("status").value("DELETED")
               );
    }
    
    private Long getMockAccountId( ) {
        return ((AccountPrincipal)getContext( ).getAuthentication( ).getPrincipal( )).getAccount( ).getId( );
    }
}
