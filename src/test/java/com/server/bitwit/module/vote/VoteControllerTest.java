package com.server.bitwit.module.vote;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.bitwit.module.account.AccountService;
import com.server.bitwit.module.ballot.BallotService;
import com.server.bitwit.module.stock.StockService;
import com.server.bitwit.util.MockJwt;
import com.server.bitwit.util.MockMvcTest;
import com.server.bitwit.util.WithMockAccount;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@MockMvcTest
@Transactional
class VoteControllerTest {
    
    @Autowired MockMvc      mockMvc;
    @Autowired MockJwt      mockJwt;
    @Autowired ObjectMapper objectMapper;
    
    @Autowired AccountService    accountService;
    @Autowired BallotService     ballotService;
    @Autowired StockService      stockService;
    @Autowired VoteService       voteService;
    @Autowired ConversionService conversionService;
    
    @Test
    @WithMockAccount
    void votePageTest( ) throws Exception {
        mockMvc.perform(get("/api/votes/search/page/type/vote-item")
                       .header("Authorization", mockJwt.getBearerToken( ))
                       .param("size", "10")
               )
               .andExpect(status( ).isOk( ));
    }

//    @Test
//    @DisplayName("")
//    void createVote( ) throws Exception {
//        // given
//        var request = new CreateVoteRequest(1L, "Hello", now( ), now( ).plusDays(1));
//
//        // then
//        mockMvc.perform(post("/api/votes")
//                       .accept(APPLICATION_JSON_UTF8)
//                       .contentType(APPLICATION_JSON)
//                       .content(objectMapper.writeValueAsString(request))
//               )
//               .andDo(print( ))
//               .andExpect(matchAll(
//                       status( ).isCreated( )
//               ));
//    }
//
//    @Test
//    @DisplayName("")
//    void getVote_responseTypeMin( ) throws Exception {
//        mockMvc.perform(get("/api/votes/1/type/min")
//                       .accept(APPLICATION_JSON_UTF8)
//               )
//               .andDo(print( ));
//    }
//
//    @Test
//    @DisplayName("")
//    void getVote_responseTypeDefault( ) throws Exception {
//        mockMvc.perform(get("/api/votes/1/type/default")
//                       .accept(APPLICATION_JSON_UTF8)
//               )
//               .andDo(print( ));
//    }
//
//    @Test
//    @DisplayName("")
//    void getVote_responseTypeClient( ) throws Exception {
//        mockMvc.perform(get("/api/votes/1/type/client")
//                       .accept(APPLICATION_JSON_UTF8)
//               )
//               .andDo(print( ));
//    }
//
//    @Test
//    @WithMockAccount
//    @DisplayName("")
//    void getVote_responseTypeVoteItem( ) throws Exception {
//        var ballotRequest = new CreateOrChangeBallotRequest(1L, VotingOption.INCREMENT);
//        var accountId     = getMockAccountId( );
//        ballotService.createOrChangeBallot(accountId, ballotRequest);
//
//        mockMvc.perform(get("/api/votes/1/type/vote-item")
//                       .accept(APPLICATION_JSON_UTF8)
//                       .header(AUTHORIZATION, mockJwt.getBearerToken( ))
//               )
//               .andDo(print( ));
//    }
//
//    @Test
//    @WithMockAccount
//    @DisplayName("")
//    void getVote_participated( ) throws Exception {
//        // given
//        var stockId       = stockService.createStock(new CreateStockRequest("AAPL", "Apple.inc", "애플")).getId( );
//        var voteRequest   = new CreateVoteRequest(stockId, "test", now( ), now( ).plusDays(1));
//        var voteId        = voteService.createVote(voteRequest).getId( );
//        var ballotRequest = new CreateOrChangeBallotRequest(voteId, VotingOption.INCREMENT);
//        var accountId     = getMockAccountId( );
//        ballotService.createOrChangeBallot(accountId, ballotRequest);
//
//        // then
//        mockMvc.perform(get("/api/votes/{voteId}/client/type/vote-item", voteId)
//                       .accept(APPLICATION_JSON_UTF8)
//                       .header(AUTHORIZATION, mockJwt.getBearerToken( ))
//               )
//               .andDo(print( ));
//    }
//
//    @Test
//    @WithMockAccount
//    @DisplayName("")
//    void searchVotes( ) throws Exception {
//        // given
//        var stockId       = stockService.createStock(new CreateStockRequest("AAPL", "Apple.inc", "애플")).getId( );
//        var voteRequest   = new CreateVoteRequest(stockId, "test", now( ), now( ).plusDays(1));
//        var voteId        = voteService.createVote(voteRequest).getId( );
//        var ballotRequest = new CreateOrChangeBallotRequest(voteId, VotingOption.INCREMENT);
//        var accountId     = getMockAccountId( );
//        ballotService.createOrChangeBallot(accountId, ballotRequest);
//
//        // then
//        mockMvc.perform(get("/api/votes/search/type/vote-item")
//                       .param("voteId", "1", "2", "3")
//                       .param("stockId", "10")
//                       .accept(APPLICATION_JSON_UTF8)
//                       .header(AUTHORIZATION, mockJwt.getBearerToken( ))
//               )
//               .andDo(print( ));
//    }
//
//    @Test
//    @WithMockAccount
//    @DisplayName("")
//    void getVoteItem_notParticipated( ) throws Exception {
//        // given
//        var stockId = stockService.createStock(new CreateStockRequest("AAPL", "Apple.inc", "애플")).getId( );
//        var request = new CreateVoteRequest(stockId, "test", now( ), now( ).plusDays(1));
//        var voteId  = voteService.createVote(request).getId( );
//
//        // then
//        mockMvc.perform(get("/api/votes/{voteId}/client/type/vote-item", voteId)
//                       .accept(APPLICATION_JSON_UTF8)
//                       .header(AUTHORIZATION, mockJwt.getBearerToken( ))
//               )
//               .andDo(print( ));
//    }
//
//    @Test
//    @DisplayName("")
//    void getVoteItemByTicker( ) {
//        // given
//
//        // when
//
//        // then
//    }
//
//    @Test
//    @DisplayName("")
//    void getVotes( ) {
//        // given
//
//        // when
//
//        // then
//    }
//
//    @Test
//    @DisplayName("")
//    void getVoteItems( ) {
//        // given
//
//        // when
//
//        // then
//    }
//
//    @Test
//    @DisplayName("")
//    void testGetVotes( ) {
//        // given
//
//        // when
//
//        // then
//    }
//
//    @Test
//    @DisplayName("")
//    void getActiveVotes( ) {
//        // given
//
//        // when
//
//        // then
//    }
//
//    @Test
//    @DisplayName("")
//    void getMyVotesParticipatedInToday( ) {
//        // given
//
//        // when
//
//        // then
//    }
//
//    @Test
//    @DisplayName("")
//    void getMyVotesByParticipationDate( ) {
//        // given
//
//        // when
//
//        // then
//    }
//
//    @Test
//    @DisplayName("")
//    void getVotesByStockId( ) {
//        // given
//
//        // when
//
//        // then
//    }
//
//    private Long getMockAccountId( ) {
//        return ((AccountPrincipal)getContext( ).getAuthentication( ).getPrincipal( )).getAccount( ).getId( );
//    }
}