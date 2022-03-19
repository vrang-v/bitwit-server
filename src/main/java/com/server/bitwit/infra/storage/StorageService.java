package com.server.bitwit.infra.storage;

import com.server.bitwit.domain.UploadFile;
import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.module.error.exception.ErrorCode;
import org.springframework.core.io.Resource;

import java.util.UUID;

public interface StorageService {
    
    UploadFile upload(Resource resource);
    
    Resource download(String filename);
    
    default String generateUploadFileName(String originalFilename) {
        return UUID.randomUUID( ) + extractFileExtension(originalFilename);
    }
    
    private String extractFileExtension(String originalFilename) {
        try {
            return originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        catch (Exception e) {
            throw new BitwitException(ErrorCode.INVALID_REQUEST, "모든 업로드 파일에는 확장자가 필요합니다." );
        }
    }
}
