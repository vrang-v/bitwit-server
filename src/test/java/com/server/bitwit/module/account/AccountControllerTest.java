package com.server.bitwit.module.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.bitwit.module.account.dto.CreateEmailAccountRequest;
import com.server.bitwit.module.account.dto.UpdateAccountRequest;
import com.server.bitwit.module.auth.dto.LoginRequest;
import com.server.bitwit.module.error.exception.ErrorCode;
import com.server.bitwit.module.security.AccountPrincipal;
import com.server.bitwit.module.security.jwt.JwtService;
import com.server.bitwit.util.MockMvcTest;
import com.server.bitwit.util.WithMockAccount;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.server.bitwit.util.ResultMatcherUtils.matchAllItem;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
@Transactional
class AccountControllerTest {
    
    @Autowired MockMvc      mockMvc;
    @Autowired ObjectMapper objectMapper;
    
    @Autowired AccountService accountService;
    @Autowired JwtService     jwtService;
    
    @Autowired AccountRepository accountRepository;
    
    @AfterEach
    void tearDown( ) {
        accountService.deleteByEmail(WithMockAccount.DEFAULT_EMAIL);
    }
    
    @ParameterizedTest
    @EnumSource
    @DisplayName("?????? ??????")
    void createAccount(CreateAccountTestSource source) throws Exception {
        // given
        var request = new CreateEmailAccountRequest(source.name, source.email, source.password);
        
        // then
        var resultActions = mockMvc.perform(post("/api/accounts")
                                           .accept(APPLICATION_JSON_UTF8)
                                           .contentType(MediaType.APPLICATION_JSON)
                                           .content(objectMapper.writeValueAsString(request))
                                   )
                                   .andDo(print( ))
                                   .andExpect(matchAll(
                                           status( ).is(source.httpStatus.value( )),
                                           matchAllItem(source.resultMatchers)
                                   ));
        
        // document
        if (source.httpStatus.is2xxSuccessful( )) {
            resultActions.andDo(document("create-account"));
        }
    }
    
    @Test
    @WithMockAccount
    @DisplayName("?????? ?????? ?????? / ?????? / 200")
    void getAccount( ) throws Exception {
        // given
        var jwt = jwtService.generateJwt(getContext( ).getAuthentication( ));
        
        // then
        mockMvc.perform(get("/api/accounts/me")
                       .accept(APPLICATION_JSON_UTF8)
                       .header(AUTHORIZATION, "Bearer " + jwt)
               )
               .andExpect(matchAll(
                       status( ).isOk( ),
                       jsonPath("accountId").exists( ),
                       jsonPath("name").exists( ),
                       jsonPath("email").exists( ),
                       jsonPath("password").doesNotExist( )
               ))
               .andDo(document("get-my-account"));
    }
    
    @Test
    @WithMockAccount
    @DisplayName("?????? ?????? ?????? / ???????????? ?????? ID / 404")
    void getAccount_nonExistentAccountId( ) throws Exception {
        // given
        var authentication = getContext( ).getAuthentication( );
        var accountId      = ((AccountPrincipal)authentication.getPrincipal( )).getAccount( ).getId( );
        var jwt            = jwtService.generateJwt(authentication);
        
        // when
        accountRepository.deleteById(accountId);
        
        // then
        mockMvc.perform(get("/api/accounts/me")
                       .accept(APPLICATION_JSON_UTF8)
                       .header(AUTHORIZATION, "Bearer " + jwt)
               )
               .andDo(print( ))
               .andExpect(matchAll(
                       status( ).isNotFound( ),
                       jsonPath("code").value(ErrorCode.RESOURCE_NOT_FOUND.getCode( ))
               ));
    }
    
    @ParameterizedTest
    @EnumSource
    @WithMockAccount
    @DisplayName("?????? ????????????")
    void updateAccount(UpdateAccountTestSource source) throws Exception {
        // given
        var request = new UpdateAccountRequest(source.name, source.email, source.password);
        var jwt     = jwtService.generateJwt(getContext( ).getAuthentication( ));
        
        // then
        mockMvc.perform(patch("/api/accounts")
                       .header(AUTHORIZATION, "Bearer " + jwt)
                       .accept(APPLICATION_JSON_UTF8)
                       .contentType(APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request))
               )
               .andExpect(matchAllItem(source.resultMatchers));
        
        if (source.password.equals("newpassword")) {
            // ?????? ??? ??????????????? ?????????
            mockMvc.perform(post("/api/login")
                           .accept(APPLICATION_JSON_UTF8)
                           .contentType(APPLICATION_JSON)
                           .content(objectMapper.writeValueAsString(new LoginRequest(source.email, "password")))
                   )
                   .andExpect(status( ).isUnauthorized( ));
            
            // ?????? ??? ??????????????? ?????????
            mockMvc.perform(post("/api/login")
                           .accept(APPLICATION_JSON_UTF8)
                           .contentType(APPLICATION_JSON)
                           .content(objectMapper.writeValueAsString(new LoginRequest(source.email, source.password)))
                   )
                   .andExpect(matchAll(
                           status( ).isOk( ),
                           jsonPath("jwt").exists( ),
                           jsonPath("accountId").exists( ),
                           jsonPath("password").doesNotExist( )
                   ));
        }
    }
    
    @ParameterizedTest
    @EnumSource
    @WithMockAccount
    @DisplayName("????????? ?????? ?????? / ?????? / 200")
    void checkForDuplicateEmail(CheckForDuplicateEmailTestSource source) throws Exception {
        mockMvc.perform(get("/api/accounts/duplicate-check/email")
                       .param("email", source.email)
                       .accept(APPLICATION_JSON_UTF8)
                       .contentType(APPLICATION_JSON)
                       .accept(APPLICATION_JSON_UTF8)
               )
               .andExpect(matchAllItem(source.resultMatchers));
    }
    
    @AllArgsConstructor
    enum CreateAccountTestSource {
        ??????_201("name", "email@email.com", "password", CREATED,
                List.of(jsonPath("accountId").exists( ),
                        jsonPath("name").exists( ),
                        jsonPath("email").exists( ),
                        jsonPath("password").doesNotExist( ),
                        jsonPath("accountType").value("EMAIL")
                )
        ),
        ?????????_??????_??????_400("name", "email", "password", BAD_REQUEST,
                List.of(jsonPath("code").value(ErrorCode.FIELD_ERROR.getCode( )))
        ),
        ??????_??????_??????_400("", "email@email.com", "password", BAD_REQUEST,
                List.of(jsonPath("code").value(ErrorCode.FIELD_ERROR.getCode( )))
        ),
        ????????????_??????_??????_400("name", "email@email.com", "pass", BAD_REQUEST,
                List.of(jsonPath("code").value(ErrorCode.FIELD_ERROR.getCode( )))
        );
        
        String              name;
        String              email;
        String              password;
        HttpStatus          httpStatus;
        List<ResultMatcher> resultMatchers;
    }
    
    @AllArgsConstructor
    enum UpdateAccountTestSource {
        ??????_200("updatedName", "updated@email.com", "newpassword",
                List.of(
                        status( ).isOk( ),
                        jsonPath("accountId").exists( ),
                        jsonPath("name").value("updatedName"),
                        jsonPath("email").value("updated@email.com"),
                        jsonPath("password").doesNotExist( )
                )
        ),
        ??????_??????_200("updatedName", null, "password",
                List.of(
                        status( ).isOk( ),
                        jsonPath("accountId").exists( ),
                        jsonPath("name").value("updatedName"),
                        jsonPath("email").exists( ),
                        jsonPath("password").doesNotExist( )
                )
        ),
        ?????????_??????_200(null, "updated@email.com", "password",
                List.of(
                        status( ).isOk( ),
                        jsonPath("accountId").exists( ),
                        jsonPath("name").exists( ),
                        jsonPath("email").value("updated@email.com"),
                        jsonPath("password").doesNotExist( )
                )
        ),
        ?????????_????????????_??????_200(null, "updated@email.com", "newpassword",
                List.of(
                        status( ).isOk( ),
                        jsonPath("accountId").exists( ),
                        jsonPath("name").exists( ),
                        jsonPath("email").value("updated@email.com"),
                        jsonPath("password").doesNotExist( )
                )
        ),
        ?????????_??????_??????_400("name", "email", "password",
                List.of(
                        status( ).isBadRequest( ),
                        jsonPath("code").value(ErrorCode.FIELD_ERROR.getCode( ))
                )
        ),
        ????????????_??????_??????_400("name", "email@email.com", "pass",
                List.of(
                        status( ).isBadRequest( ),
                        jsonPath("code").value(ErrorCode.FIELD_ERROR.getCode( ))
                )
        );
        
        String              name;
        String              email;
        String              password;
        List<ResultMatcher> resultMatchers;
    }
    
    @AllArgsConstructor
    enum CheckForDuplicateEmailTestSource {
        ??????_200(Constants.DUPLICATE_EMAIL,
                List.of(
                        status( ).isOk( ),
                        jsonPath("email").value(Constants.DUPLICATE_EMAIL),
                        jsonPath("duplicate").value(true)
                )
        ),
        
        ?????????_200(Constants.NON_DUPLICATE_EMAIL,
                List.of(
                        status( ).isOk( ),
                        jsonPath("email").value(Constants.NON_DUPLICATE_EMAIL),
                        jsonPath("duplicate").value(false)
                )
        );
//        ?????????_??????_400("non-email-format",
//                List.of(
//                        status( ).isBadRequest( ),
//                        jsonPath("code").value(ErrorCode.FIELD_ERROR.getCode( ))
//                )
//        );
        
        String              email;
        List<ResultMatcher> resultMatchers;
        
        private static class Constants {
            public static final String DUPLICATE_EMAIL     = WithMockAccount.DEFAULT_EMAIL;
            public static final String NON_DUPLICATE_EMAIL = "non-duplicate-test@email.com";
        }
    }
}
