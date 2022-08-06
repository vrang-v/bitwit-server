package com.server.bitwit.module.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.bitwit.module.post.dto.CreatePostRequest;
import com.server.bitwit.util.MockJwt;
import com.server.bitwit.util.MockMvcTest;
import com.server.bitwit.util.WithMockAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
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
    
    @Test
    @WithMockAccount
    void createPost( ) throws Exception {
        // given
        var request = new CreatePostRequest( );
        request.setTitle("제목입니다");
        request.setContent("내용입니다");
        
        // then
        mockMvc.perform(post("/api/posts")
                       .header(AUTHORIZATION, mockJwt.getBearerToken( ))
                       .contentType(APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request))
               )
               .andExpectAll(
                       status( ).isCreated( ),
                       jsonPath("$.postId").exists( ),
                       jsonPath("$.title").value("제목입니다"),
                       jsonPath("$.content").value("내용입니다")
               );
    }
    
    @Test
    @WithMockAccount
    void createPost_withStock( ) throws Exception {
        // given
        var request = new CreatePostRequest( );
        request.setTitle("제목입니다");
        request.setContent("내용입니다");
        request.setTickers(List.of("BTC"));
        
        // then
        mockMvc.perform(post("/api/posts")
                       .header(AUTHORIZATION, mockJwt.getBearerToken( ))
                       .contentType(APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request))
               )
               .andExpectAll(
                       status( ).isCreated( ),
                       jsonPath("$.postId").exists( ),
                       jsonPath("$.title").value("제목입니다"),
                       jsonPath("$.content").value("내용입니다"),
                       jsonPath("$.stocks[0].ticker").value("BTC")
               );
    }
    
    @Test
    @WithMockAccount
    void search( ) throws Exception {
        mockMvc.perform(get("/api/posts/search")
                       .header(AUTHORIZATION, mockJwt.getBearerToken( ))
                       .param("ticker", "BTC")
               )
               .andExpectAll(
                       status( ).isOk( )
               );
    }
}