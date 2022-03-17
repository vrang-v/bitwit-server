package com.server.bitwit.module.security.jwt;

import com.google.common.net.HttpHeaders;
import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.module.error.exception.ErrorCode;
import com.server.bitwit.module.security.AccountPrincipal;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {
    
    private final JwtProperties jwtProperties;
    
    public String generateJwt(Authentication authentication) {
        var authorities = authentication.getAuthorities( ).stream( )
                                        .map(GrantedAuthority::getAuthority)
                                        .collect(Collectors.joining(","));
        var claims = Map.of(
                Claim.ACCOUNT_ID.getKey( ), ((AccountPrincipal)authentication.getPrincipal( )).getAccount( ).getId( ),
                Claim.AUTHORITIES.getKey( ), authorities
        );
        
        return Jwts.builder( )
                   .setClaims(claims)
                   .setSubject(authentication.getName( ))
                   .setExpiration(jwtProperties.getExpirationDate( ))
                   .signWith(jwtProperties.getSecretKey( ), SignatureAlgorithm.HS512)
                   .compact( );
    }
    
    public String extractJwt(HttpServletRequest httpRequest) {
        var bearerToken = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        return hasText(bearerToken) && bearerToken.startsWith("Bearer ") ? bearerToken.replace("Bearer ", "") : null;
    }
    
    public Object extractClaim(String jwt, Claim claim) {
        try {
            return Jwts.parserBuilder( )
                       .setSigningKey(jwtProperties.getSecretKey( ))
                       .build( )
                       .parseClaimsJws(jwt)
                       .getBody( )
                       .get(claim.getKey( ), claim.getType( ));
        }
        catch (MalformedJwtException e) {
            throw new BitwitException(ErrorCode.INVALID_JWT);
        }
        catch (Exception e) {
            e.printStackTrace( );
            throw new BitwitException(ErrorCode.AUTHENTICATION_FAILED);
        }
    }
    
    public Authentication extractAuthentication(String jwt) {
        var claims = Jwts
                .parserBuilder( )
                .setSigningKey(jwtProperties.getSecretKey( ))
                .build( )
                .parseClaimsJws(jwt)
                .getBody( );
        
        var authorities = stream(claims.get(Claim.AUTHORITIES.getKey( )).toString( ).split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList( ));
        
        var user = new User(claims.getSubject( ), jwt, authorities);
        return new UsernamePasswordAuthenticationToken(user, jwt, authorities);
    }
    
    public boolean isValidJwt(String jwt) {
        if (! StringUtils.hasText(jwt)) { return false; }
        try {
            Jwts.parserBuilder( ).setSigningKey(jwtProperties.getSecretKey( )).build( ).parseClaimsJws(jwt);
            return true;
        }
        catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        }
        catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        }
        catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        }
        catch (JwtException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
