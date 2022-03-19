package com.server.bitwit.infra.storage.cloud.gcp;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.server.bitwit.domain.UploadFile;
import com.server.bitwit.infra.storage.StorageService;
import com.server.bitwit.module.error.exception.BitwitException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gcp.storage.GoogleStorageResource;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Profile("!local")
@Service
public class GoogleCloudStorageService implements StorageService {
    
    private static final String BUCKET_NAME = "bitwit-profile-image";
    
    private final Storage storage;
    
    @Override
    public UploadFile upload(Resource resource) {
        if (! resource.exists( )) {
            throw new BitwitException("Failed to store empty file " + resource.getFilename( ));
        }
        
        try {
            var originalFilename = resource.getFilename( );
            var uploadFileName   = generateUploadFileName(originalFilename);
            new GoogleStorageResource(storage, String.format("gs://%s/%s", BUCKET_NAME, uploadFileName))
                    .createBlob(resource.getInputStream( ).readAllBytes( ));
            return new UploadFile(originalFilename, uploadFileName);
        }
        catch (NullPointerException e) {
            throw new BitwitException("Failed to store unnamed file " + resource.getFilename( ));
        }
        catch (IOException e) {
            throw new BitwitException("Failed to store file " + resource.getFilename( ));
        }
        catch (StorageException e) {
            throw new BitwitException(e.getMessage( ));
        }
    }
    
    @Override
    public Resource download(String filename) {
        return new GoogleStorageResource(storage, String.format("gs://%s/%s", BUCKET_NAME, filename));
    }
}
