package com.server.bitwit.cofig;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;

@TestConfiguration
public class MockMvcConfig {
    
    @Bean
    public CharacterEncodingFilter characterEncodingFilter( ) {
        return new CharacterEncodingFilter(StandardCharsets.UTF_8.name( ), true);
    }
    
}
