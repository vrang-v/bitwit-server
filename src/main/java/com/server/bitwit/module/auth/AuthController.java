package com.server.bitwit.module.auth;

import com.server.bitwit.infra.client.google.GoogleService;
import com.server.bitwit.module.account.AccountService;
import com.server.bitwit.module.auth.dto.GoogleLoginRequest;
import com.server.bitwit.module.auth.dto.LoginRequest;
import com.server.bitwit.module.auth.dto.LoginResponse;
import com.server.bitwit.module.error.exception.InvalidJwtException;
import com.server.bitwit.module.security.AccountPrincipal;
import com.server.bitwit.module.security.SecurityService;
import com.server.bitwit.module.security.basic.UserDetailsPrincipal;
import com.server.bitwit.module.security.jwt.JwtService;
import com.server.bitwit.module.security.jwt.support.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {
    
    private final AccountService  accountService;
    private final SecurityService securityService;
    private final JwtService      jwtService;
    private final GoogleService   googleService;
    
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        var authentication = securityService.authenticate(request.getEmail( ), request.getPassword( ));
        var account        = ((UserDetailsPrincipal)authentication.getPrincipal( )).getAccount( );
        var jwt            = jwtService.generateJwt(authentication);
        return new LoginResponse(account, jwt);
    }
    
    @PostMapping("/login/jwt")
    public LoginResponse jwtLogin(@Jwt Long accountId, @RequestHeader(AUTHORIZATION) String token) {
        var account = accountService.findById(accountId);
        if (account.isEmpty( )) {
            throw new InvalidJwtException("존재하지 accountId[" + accountId + "]로 JWT 로그인 시도");
        }
        return new LoginResponse(account.get( ), token.replace("Bearer ", ""));
    }
    
    @PostMapping("/login/google")
    public Mono<LoginResponse> googleLogin(@Valid @RequestBody GoogleLoginRequest request) {
        return googleService.resolveIdToken(request.getIdToken( ))
                            .map(securityService::authenticate)
                            .map(authentication -> {
                                var account = ((AccountPrincipal)authentication.getPrincipal( )).getAccount( );
                                var jwt     = jwtService.generateJwt(authentication);
                                return new LoginResponse(account, jwt);
                            });
    }
    
    @GetMapping("/login/oauth2/complete")
    public LoginResponse afterOAuth2Login(@RequestParam Long accountId, @RequestParam String jwt) {
        var account = accountService.findById(accountId).orElseThrow( );
        return new LoginResponse(account, jwt);
    }
}
