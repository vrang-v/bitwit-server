package com.server.bitwit.infra.storage;

import com.server.bitwit.domain.UploadFile;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface StorageService {
    
    UploadFile upload(MultipartFile file);
    
    Resource download(String filename);
    
    default String generateUploadFileName(String extension) {
        return UUID.randomUUID( ) + extension;
    }
    
    default String extractFileExtension(String originalFilename) {
        try {
            return originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        catch (Exception e) {
            return "";
        }
    }
}
