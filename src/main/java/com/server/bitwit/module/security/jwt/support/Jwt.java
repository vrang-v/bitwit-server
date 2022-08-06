package com.server.bitwit.module.security.jwt.support;

import com.server.bitwit.module.security.jwt.Claim;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Jwt {
    
    @AliasFor("claim")
    Claim value( ) default Claim.ACCOUNT_ID;
    
    @AliasFor("value")
    Claim claim( ) default Claim.ACCOUNT_ID;
    
}
