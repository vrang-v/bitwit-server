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
    void transaction( ) throws IOException {
        var text           = "Hello Storage Service!";
        var uploadFile     = storageService.upload("test.txt", text.getBytes(Charset.defaultCharset( )));
        var uploadFileName = uploadFile.getUploadFileName( );
        var downloadFile   = storageService.download(uploadFileName);
        var downloadText = new BufferedReader(new InputStreamReader(downloadFile.getInputStream( )))
                .lines( )
                .collect(Collectors.joining( ));
        
        assertThat(text).isEqualTo(downloadText);
        storageService.delete(uploadFileName);
    }
}
