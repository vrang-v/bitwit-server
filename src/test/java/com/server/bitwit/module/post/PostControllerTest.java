package com.server.bitwit.module.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.bitwit.module.account.AccountService;
import com.server.bitwit.module.post.dto.CreatePostRequest;
import com.server.bitwit.util.MockJwt;
import com.server.bitwit.util.MockMvcTest;
import com.server.bitwit.util.WithMockAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
@Transactional
class PostControllerTest {
    
    @Autowired MockMvc      mockMvc;
    @Autowired MockJwt      mockJwt;
    @Autowired ObjectMapper objectMapper;
    
    @Autowired AccountService accountService;
    
    @AfterEach
    void tearDown( ) {
        accountService.deleteByEmail(WithMockAccount.DEFAULT_EMAIL);
    }
    
    @Test
    @WithMockAccount
    void createPost( ) throws Exception {
        // given
        var request = new CreatePostRequest( );
        request.setTitle("시황 정리");
        request.setContent("나스닥 폭락");
        request.setTickers(List.of("BTC"));
        
        // then
        mockMvc.perform(post("/api/posts")
                       .accept(APPLICATION_JSON_UTF8)
                       .header(AUTHORIZATION, mockJwt.getBearerToken( ))
                       .contentType(APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request))
               )
               .andExpect(matchAll(
                       status( ).isCreated( ),
                       jsonPath("$.postId").exists( ),
                       jsonPath("$.title").value("시황 정리"),
                       jsonPath("$.content").value("나스닥 폭락")
               ));
    }
    
    @Test
    @WithMockAccount
    void createPost_withStock( ) throws Exception {
        // given
        var request = new CreatePostRequest( );
        request.setTitle("시황 정리");
        request.setContent("나스닥 폭락");
        request.setTickers(List.of("BTC"));
        
        // then
        mockMvc.perform(post("/api/posts")
                       .accept(APPLICATION_JSON_UTF8)
                       .header(AUTHORIZATION, mockJwt.getBearerToken( ))
                       .contentType(APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request))
               )
               .andExpect(matchAll(
                       status( ).isCreated( )
               ));
    }
    
    @Test
    @WithMockAccount
    void search( ) throws Exception {
        mockMvc.perform(get("/api/posts/search")
                       .accept(APPLICATION_JSON_UTF8)
                       .header(AUTHORIZATION, mockJwt.getBearerToken( ))
                       .param("ticker", "BTC")
               )
               .andExpect(matchAll(
                       status( ).isOk( )
               ));
    }
}