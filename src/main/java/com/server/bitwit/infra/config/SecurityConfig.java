package com.server.bitwit.infra.config;

import com.server.bitwit.module.security.jwt.JwtService;
import com.server.bitwit.module.security.jwt.support.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    private final JwtService jwtService;
    
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService;
    
    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;
    
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    
    @Bean
    public PasswordEncoder passwordEncoder( ) {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder( );
    }
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean( ) throws Exception {
        return super.authenticationManagerBean( );
    }
    
    @Override
    public void configure(WebSecurity web) {
        web.ignoring( )
           .antMatchers(
                   "/h2-console/**",
                   "/favicon.ico",
                   "/error"
           )
           .requestMatchers(PathRequest.toStaticResources( ).atCommonLocations( ));
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors( )
                .and( )
                
                .csrf( ).disable( )
                .formLogin( ).disable( )
                .httpBasic( ).disable( )
                
                .addFilterBefore(new JwtFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
                
                .sessionManagement( )
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and( )
                
                .authorizeRequests( )
                .antMatchers("/api/**").permitAll( )
                .antMatchers("/api/v1/accounts/**").permitAll( )
                .antMatchers("/api/v1/login/**").permitAll( )
                .anyRequest( ).authenticated( )
                .and( )
                
                .oauth2Login( )
                .authorizationEndpoint( )
                .baseUri("/login/oauth2/authorize")
                .authorizationRequestRepository(authorizationRequestRepository)
                .and( )
                .userInfoEndpoint( )
                .userService(oauth2UserService)
                .and( )
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler);
    }
}
