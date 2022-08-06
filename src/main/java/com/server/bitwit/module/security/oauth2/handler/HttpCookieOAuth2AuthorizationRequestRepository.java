package com.server.bitwit.module.security.oauth2.handler;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.server.bitwit.util.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REDIRECT_URI;

@Slf4j
@Component
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    
    private static final String OAUTH2_AUTHORIZATION_REQUEST = "oauth2_auth_request";
    private static final int    COOKIE_EXPIRATION_SECONDS    = 180;
    
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST)
                          .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                          .orElse(null);
    }
    
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequestCookies(request, response);
            return;
        }
    
        var serializedRequest = CookieUtils.serialize(authorizationRequest);
        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST, serializedRequest, COOKIE_EXPIRATION_SECONDS);
        
        var redirectUriAfterLogin = request.getParameter(REDIRECT_URI);
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtils.addCookie(response, REDIRECT_URI, redirectUriAfterLogin, COOKIE_EXPIRATION_SECONDS);
        }
    }
    
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return this.loadAuthorizationRequest(request);
    }
    
    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI);
    }
}
