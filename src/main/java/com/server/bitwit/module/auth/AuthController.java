package com.server.bitwit.module.auth;

import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.module.security.SecurityService;
import com.server.bitwit.module.security.jwt.Jwt;
import com.server.bitwit.module.security.jwt.JwtService;
import com.server.bitwit.module.account.AccountService;
import com.server.bitwit.module.auth.dto.LoginRequest;
import com.server.bitwit.module.auth.dto.LoginResponse;
import com.server.bitwit.module.security.basic.UserDetailsPrincipal;
import com.server.bitwit.module.common.dto.SimpleIdResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AuthController {
    
    private final AccountService  accountService;
    private final SecurityService securityService;
    private final JwtService      jwtService;
    
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        var authentication = securityService.authenticate(request.getEmail( ), request.getPassword( ));
        var account        = ((UserDetailsPrincipal)authentication.getPrincipal( )).getAccount( );
        var jwt            = jwtService.generateJwt(authentication);
        
        response.setHeader(AUTHORIZATION, "Bearer " + jwt);
        return new LoginResponse(account, jwt);
    }
    
    @PostMapping("/login/jwt")
    public SimpleIdResponse jwtLogin(@Jwt Long accountId) {
        if (! accountService.existsById(accountId)) {
            throw new BitwitException("존재하지 않는 계정으로 JWT 로그인 시도");
        }
        return SimpleIdResponse.of(accountId);
    }
    
    @GetMapping("/login/oauth2/complete")
    public LoginResponse afterOAuth2Login(@RequestParam Long accountId, @RequestParam String jwt, HttpServletResponse response) {
        var account = accountService.getAccount(accountId).orElseThrow( );
        
        response.setHeader(AUTHORIZATION, "Bearer " + jwt);
        return new LoginResponse(account, jwt);
    }
}
