package com.server.bitwit.module.security.oauth2.handler;

import com.server.bitwit.module.security.jwt.JwtService;
import com.server.bitwit.module.security.oauth2.OAuth2Properties;
import com.server.bitwit.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REDIRECT_URI;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    private final JwtService jwtService;
    
    private final OAuth2Properties oAuth2Properties;
    
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
    throws IOException {
        var targetUrl = determineTargetUrl(request, response, authentication);
        if (response.isCommitted( )) {
            logger.debug("Response has already bean committed. Unable to redirect to " + targetUrl);
            return;
        }
        clearAuthenticationAttributes(request, response);
        
        getRedirectStrategy( ).sendRedirect(request, response, targetUrl);
    }
    
    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        var jwt = jwtService.generateJwt(authentication);
        CookieUtils.addCookie(response, "jwt", jwt, 18000);
        
        return CookieUtils.getCookie(request, REDIRECT_URI).map(Cookie::getValue)
                   .filter(this::isAuthorizedRedirectUri)
                   .orElseGet(oAuth2Properties::getDefaultRedirectUri);
    }
    
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
    
    private boolean isAuthorizedRedirectUri(String uri) {
        var redirectUri = URI.create(uri);
        return oAuth2Properties.getAuthorizedRedirectUris( )
                               .stream( )
                               .map(URI::create)
                               .anyMatch(authorizedUri ->
                                       authorizedUri.getHost( ).equalsIgnoreCase(redirectUri.getHost( ))
                                       && authorizedUri.getPort( ) == redirectUri.getPort( )
                               );
    }
}
