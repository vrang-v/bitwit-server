package com.server.bitwit.module.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.bitwit.module.account.AccountRepository;
import com.server.bitwit.module.auth.dto.LoginRequest;
import com.server.bitwit.module.error.exception.ErrorCode;
import com.server.bitwit.module.security.jwt.JwtService;
import com.server.bitwit.util.MockMvcTest;
import com.server.bitwit.util.WithMockAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
@Transactional
class AuthControllerTest {
    
    @Autowired MockMvc      mockMvc;
    @Autowired ObjectMapper objectMapper;
    
    @Autowired JwtService jwtService;
    @Autowired AccountRepository accountRepository;
    
    @Test
    @WithMockAccount
    void login( ) throws Exception {
        // given
        var request = new LoginRequest(WithMockAccount.DEFAULT_EMAIL, WithMockAccount.DEFAULT_PASSWORD);
        
        // then
        mockMvc.perform(post("/api/login")
                       .accept(APPLICATION_JSON_UTF8)
                       .contentType(APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request))
               )
               .andExpect(matchAll(
                       status( ).isOk( ),
                       jsonPath("jwt").exists( ),
                       jsonPath("accountId").exists( ),
                       jsonPath("email").exists( ),
                       jsonPath("password").doesNotExist( )
               ));
    }
    
    @Test
    void login_nonExistentEmail( ) throws Exception {
        // given
        var request = new LoginRequest("unregistered@email.com", "password");
        
        // then
        mockMvc.perform(post("/api/login")
                       .accept(APPLICATION_JSON_UTF8)
                       .contentType(APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request))
               )
               .andExpect(matchAll(
                       status( ).isUnauthorized( ),
                       jsonPath("code").value(ErrorCode.AUTHENTICATION_FAILED.getCode( ))
               ));
    }
    
    @Test
    @WithMockAccount
    void login_wrongPassword( ) throws Exception {
        // given
        var request = new LoginRequest(WithMockAccount.DEFAULT_EMAIL, "wrong" + WithMockAccount.DEFAULT_PASSWORD);
        
        // then
        mockMvc.perform(post("/api/login")
                       .accept(APPLICATION_JSON_UTF8)
                       .contentType(APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request))
               )
               .andExpect(matchAll(
                       status( ).isUnauthorized( ),
                       jsonPath("code").value(ErrorCode.AUTHENTICATION_FAILED.getCode( ))
               ));
    }
    
    @Test
    @WithMockAccount
    void jwtLogin( ) throws Exception {
        // given
        var jwt = "Bearer " + jwtService.generateJwt(SecurityContextHolder.getContext( ).getAuthentication( ));
        
        // then
        mockMvc.perform(post("/api/login/jwt")
                       .header(AUTHORIZATION, jwt)
               )
               .andDo(print( ))
               .andExpect(matchAll(
                       status( ).isOk( )
               ));
    }
    
    @Test
    @WithMockAccount
    void jwtLogin_deletedUserJwt( ) throws Exception {
        // given
        var jwt = "Bearer " + jwtService.generateJwt(SecurityContextHolder.getContext( ).getAuthentication( ));
        accountRepository.deleteByEmail(WithMockAccount.DEFAULT_EMAIL);
        
        // then
        mockMvc.perform(post("/api/login/jwt")
                       .accept(APPLICATION_JSON_UTF8)
                       .header(AUTHORIZATION, jwt)
               )
               .andDo(print( ))
               .andExpect(matchAll(
                       status( ).isUnauthorized( ),
                       jsonPath("code").value(ErrorCode.AUTHENTICATION_FAILED.getCode( ))
               ));
    }
    
    @Test
    @WithMockAccount
    void jwtLogin_invalidJwt_401( ) throws Exception {
        // given
        var jwt = "Bearer "+ jwtService.generateJwt(SecurityContextHolder.getContext( ).getAuthentication( )) + "wrong";
        
        // then
        mockMvc.perform(post("/api/login/jwt")
                       .accept(APPLICATION_JSON_UTF8)
                       .header(AUTHORIZATION, jwt)
               )
               .andDo(print( ))
               .andExpect(matchAll(
                       status( ).isUnauthorized( ),
                       jsonPath("code").value(ErrorCode.AUTHENTICATION_FAILED.getCode( ))
               ));
    }
}
