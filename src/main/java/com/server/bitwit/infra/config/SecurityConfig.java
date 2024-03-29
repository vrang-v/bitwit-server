package com.server.bitwit.infra.config;

import com.server.bitwit.module.security.basic.UsernamePasswordAuthenticationProvider;
import com.server.bitwit.module.security.google.GoogleLoginAuthenticationProvider;
import com.server.bitwit.module.security.google.GoogleUserService;
import com.server.bitwit.module.security.jwt.JwtService;
import com.server.bitwit.module.security.jwt.support.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    
    private final JwtService jwtService;
    
    private final UserDetailsService                               userDetailsService;
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService;
    private final GoogleUserService                                googleUserService;
    
    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;
    
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    
    @Bean
    public PasswordEncoder passwordEncoder( ) {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder( );
    }
    
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder)
    throws Exception {
        return httpSecurity
                .getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(new GoogleLoginAuthenticationProvider(googleUserService))
                .authenticationProvider(new UsernamePasswordAuthenticationProvider(userDetailsService, passwordEncoder))
                .build( );
    }
    
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer( ) {
        return web ->
                web.ignoring( )
                   .antMatchers(
                           "/h2-console/**",
                           "/favicon.ico",
                           "/error"
                   )
                   .requestMatchers(PathRequest.toStaticResources( ).atCommonLocations( ));
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
    throws Exception {
        return httpSecurity
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
                .antMatchers("/home").permitAll( )
                .antMatchers("/accounts/**").permitAll( )
                .antMatchers("/api/**").permitAll( )
                .anyRequest( ).authenticated( )
                
                .and( )
                .oauth2Login( )
                .authorizationEndpoint( )
                .baseUri("/api/login/oauth2/authorize/**")
                .authorizationRequestRepository(authorizationRequestRepository)
                .and( )
                .redirectionEndpoint( )
                .baseUri("/api/login/oauth2/callback/**")
                .and( )
                .userInfoEndpoint( )
                .userService(oauth2UserService)
                .and( )
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                
                .and( )
                .build( );
    }
}
