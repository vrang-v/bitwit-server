package com.server.bitwit.infra.client.google;

import com.server.bitwit.infra.client.google.dto.GoogleUser;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GoogleService {
    
    public static final String GOOGLE_OAUTH2_URL = "https://oauth2.googleapis.com";
    
    public Mono<GoogleUser> resolveIdToken(String idToken) {
        return WebClient
                .create(GOOGLE_OAUTH2_URL)
                .get( )
                .uri(uri -> uri.path("/tokeninfo")
                               .queryParam("id_token", idToken)
                               .build( )
                )
                .retrieve( )
                .bodyToMono(GoogleUser.class);
    }
}
