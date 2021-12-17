package com.server.bitwit.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UploadFile {
    
    private String originFileName;
    
    private String uploadFileName;
    
}
