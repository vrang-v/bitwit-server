package com.server.bitwit.util;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockAccountSecurityContextFactory.class)
public @interface WithMockAccount {
    
    String DEFAULT_USERNAME = "mockuser";
    String DEFAULT_EMAIL    = "mock@email.com";
    String DEFAULT_PASSWORD = "password";
    
    String username( ) default DEFAULT_USERNAME;

    String email( ) default DEFAULT_EMAIL;

    String password( ) default DEFAULT_PASSWORD;
}
