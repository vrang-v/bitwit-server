package com.server.bitwit.infra.client.google.dto;

import lombok.Data;

@Data
public class GoogleUser {
  
    private String iss;
    private String azp;
    private String aud;
    private String sub;
    private String email;
    private String emailVerified;
    private String name;
    private String picture;
    private String givenName;
    private String familyName;
    private String locale;
    private String iat;
    private String exp;
    private String alg;
    private String kid;
    
}
