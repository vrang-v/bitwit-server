package com.server.bitwit.module.ballot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.bitwit.module.account.AccountService;
import com.server.bitwit.module.account.dto.AccountRequest;
import com.server.bitwit.module.ballot.dto.BallotRequest;
import com.server.bitwit.module.domain.VotingOption;
import com.server.bitwit.module.stock.StockService;
import com.server.bitwit.module.stock.dto.StockRequest;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class BallotControllerTest
{
    @Autowired MockMvc      mockMvc;
    @Autowired ObjectMapper objectMapper;
    
    @Autowired BallotService  ballotService;
    @Autowired AccountService accountService;
    @Autowired StockService   stockService;
    
    @Test
    @DisplayName("Ballot 생성 / 정상 / 201")
    void createBallot_normal_201( ) throws Exception
    {
        // given
        var accountId        = accountService.createAccount(new AccountRequest("NAME", "email@a.com", "plainpassword"));
        var stockId          = stockService.createStock(new StockRequest("AAPL"));
        var ballotRequest    = new BallotRequest(accountId, stockId, VotingOption.INCREMENT);
        var expectedResponse = new JSONObject( ).put("id", 1).toString( );
        
        // when, then
        mockMvc.perform(post("/ballots")
                       .content(objectMapper.writeValueAsString(ballotRequest))
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status( ).isCreated( ))
               .andExpect(content( ).json(expectedResponse));
    }
    
    @Test
    @DisplayName("Ballot 생성 / 존재하지 않는 AccountId / 400")
    void createBallot_nonExistentAccountId_400( ) throws Exception
    {
        // given
        var stockId          = stockService.createStock(new StockRequest("AAPL"));
        var ballotRequest    = new BallotRequest(-1L, stockId, VotingOption.INCREMENT);
        
        // when, then
        mockMvc.perform(post("/ballots")
                       .content(objectMapper.writeValueAsString(ballotRequest))
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status( ).isNotFound( ));
    }
    
    @Test
    @DisplayName("Ballot 생성 / 존재하지 않는 StockId / 400")
    void createBallot_nonExistentStockId_400( ) throws Exception
    {
        // given
        var accountId        = accountService.createAccount(new AccountRequest("NAME", "email@a.com", "plainpassword"));
        var ballotRequest    = new BallotRequest(accountId, -1L, VotingOption.INCREMENT);
        
        // when, then
        mockMvc.perform(post("/ballots")
                       .content(objectMapper.writeValueAsString(ballotRequest))
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status( ).isNotFound( ));
    }
    
    @Test
    @DisplayName("Ballot 생성 / 중복 투표 / 400")
    void createBallot_duplicateVoting_400( ) throws Exception
    {
        // given
        var accountId        = accountService.createAccount(new AccountRequest("NAME", "email@a.com", "plainpassword"));
        var stockId          = stockService.createStock(new StockRequest("AAPL"));
        var ballotRequest    = new BallotRequest(accountId, stockId, VotingOption.INCREMENT);
        ballotService.createBallot(ballotRequest);
    
        // when, then
        mockMvc.perform(post("/ballots")
                       .content(objectMapper.writeValueAsString(ballotRequest))
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status( ).isBadRequest( ));
    }
}
