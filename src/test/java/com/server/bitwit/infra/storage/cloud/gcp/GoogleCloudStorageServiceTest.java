package com.server.bitwit.infra.storage.cloud.gcp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class GoogleCloudStorageServiceTest {
    
    @Autowired GoogleCloudStorageService storageService;
    
    @Test
    void existGoogleApplicationCredentialsEnvVariable( ) {
        assertThat(System.getenv("GOOGLE_APPLICATION_CREDENTIALS")).isNotNull( );
    }
    
    @Test
    void transaction( ) throws IOException {
        // given
        var fileName = "test.txt";
        var content  = "Hello Storage Service!";
        
        // when
        var uploadFile     = storageService.upload(fileName, content.getBytes(Charset.defaultCharset( )));
        var uploadFileName = uploadFile.getUploadFileName( );
        var downloadFile   = storageService.download(uploadFileName);
        var downloadContent = new BufferedReader(new InputStreamReader(downloadFile.getInputStream( )))
                .lines( )
                .collect(Collectors.joining( ));
        
        //then
        assertThat(content).isEqualTo(downloadContent);
        
        storageService.delete(uploadFileName);
    }
}
