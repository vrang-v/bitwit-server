package com.server.bitwit.infra.storage.local;

import com.server.bitwit.domain.UploadFile;
import com.server.bitwit.infra.storage.StorageService;
import com.server.bitwit.module.error.exception.BitwitException;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

@Profile("local")
@Service
public class LocalFileStorageService implements StorageService {
    
    @Override
    public UploadFile upload(Resource resource) {
        if (! resource.exists( )) {
            throw new BitwitException("Failed to store empty file " + resource.getFilename( ));
        }
        
        try {
            var originalFilename = resource.getFilename( );
            var uploadFileName   = generateUploadFileName(originalFilename);
            Files.write(getAbsolutePath(uploadFileName), resource.getInputStream( ).readAllBytes( ));
            return new UploadFile(originalFilename, uploadFileName);
        }
        catch (NullPointerException e) {
            throw new BitwitException("Failed to store unnamed file " + resource.getFilename( ));
        }
        catch (IOException e) {
            throw new BitwitException("Failed to store file " + resource.getFilename( ));
        }
    }
    
    @Override
    public Resource download(String filename) {
        try {
            return new UrlResource("file:" + getAbsolutePath(filename));
        }
        catch (MalformedURLException e) {
            throw new BitwitException("File not found " + filename);
        }
    }
    
    public Path getAbsolutePath(String uploadFileName) {
        return Path.of("C:\\Users\\pc\\Desktop\\bitwit\\bitwit-server\\storage\\", uploadFileName);
    }
}
