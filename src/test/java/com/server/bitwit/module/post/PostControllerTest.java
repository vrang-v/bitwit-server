package com.server.bitwit.module.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.bitwit.module.post.dto.CreatePostRequest;
import com.server.bitwit.util.MockJwt;
import com.server.bitwit.util.MockMvcTest;
import com.server.bitwit.util.WithMockAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class PostControllerTest {
    
    @Autowired MockMvc      mockMvc;
    @Autowired MockJwt      mockJwt;
    @Autowired ObjectMapper objectMapper;
    
    @Test
    @WithMockAccount
    void createPost( ) throws Exception {
        // given
        var request = new CreatePostRequest( );
        request.setTitle("시황 정리");
        request.setContent("나스닥 폭락");
        
        // then
        mockMvc.perform(post("/api/posts")
                       .accept(APPLICATION_JSON_UTF8)
                       .header(AUTHORIZATION, mockJwt.getBearerToken( ))
                       .contentType(APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request))
               )
               .andDo(print( ))
               .andExpect(matchAll(
                       status( ).isCreated( )
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
               .andDo(print( ))
               .andExpect(matchAll(
                       status( ).isCreated( )
               ));
    }
    
    @Test
    void search( ) throws Exception {
        mockMvc.perform(get("/api/posts/search")
                       .accept(APPLICATION_JSON_UTF8)
                       .param("ticker", "BTC")
               )
               .andDo(print( ));
    }
}