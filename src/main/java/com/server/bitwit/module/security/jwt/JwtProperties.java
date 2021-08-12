package com.server.bitwit.module.security.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Data
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {
    
    private String secret;
    private Long   validityInMillis;
    
    public SecretKey getSecretKey( ) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
    
    public Date getExpirationDate( ) {
        return new Date(System.currentTimeMillis( ) + validityInMillis);
    }
}
