package com.server.bitwit.infra.storage.cloud.gcp;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.server.bitwit.domain.UploadFile;
import com.server.bitwit.infra.storage.StorageService;
import com.server.bitwit.module.error.exception.BitwitException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Profile("!local")
@Service
public class GoogleCloudStorageService implements StorageService {
    
    private static final String BUCKET_NAME = "bitwit-profile-image";
    
    private final Storage storage;
    
    @Override
    public UploadFile upload(MultipartFile file) {
        if (file.isEmpty( )) {
            throw new BitwitException("Failed to store empty file " + file.getOriginalFilename( ));
        }
        
        var originalFilename = file.getOriginalFilename( );
        if (originalFilename == null) {
            throw new BitwitException("Failed to store unnamed file " + file.getOriginalFilename( ));
        }
        
        var fileExtension  = extractFileExtension(originalFilename);
        var uploadFileName = generateUploadFileName(fileExtension);
        
        var blobInfo = BlobInfo.newBuilder(BUCKET_NAME, uploadFileName)
                               .build( );
        try {
            storage.create(blobInfo, file.getBytes( ));
        }
        catch (IOException e) {
            throw new BitwitException("Failed to store file " + originalFilename);
        }
        
        return new UploadFile(originalFilename, uploadFileName);
    }
    
    @Override
    public Resource download(String filename) {
        var blob = storage.get(BUCKET_NAME, filename);
        return new ByteArrayResource(blob.getContent( ));
    }
}
