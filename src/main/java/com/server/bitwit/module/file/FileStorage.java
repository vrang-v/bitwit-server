package com.server.bitwit.module.file;

import com.server.bitwit.domain.UploadFile;
import com.server.bitwit.module.error.exception.BitwitException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Component
public class FileStorage {
    
    @SneakyThrows
    public UploadFile upload(MultipartFile file) {
        if (file.isEmpty( )) {
            throw new BitwitException("Failed to store empty file " + file.getOriginalFilename( ));
        }
        
        var originalFilename = file.getOriginalFilename( );
        var uploadFileName   = createUploadFileName(originalFilename);
        
        file.transferTo(new File(getFullPath(uploadFileName)));
        
        return new UploadFile(originalFilename, uploadFileName);
    }
    
    public String getFullPath(String uploadFileName) {
        return "C:\\Users\\pc\\Desktop\\bitwit\\bitwit-server\\image\\" + uploadFileName;
    }
    
    private String createUploadFileName(String originalFilename) {
        var fileExtension = extractFileExtension(originalFilename);
        return UUID.randomUUID( ) + fileExtension;
    }
    
    private String extractFileExtension(String originalFilename) {
        try {
            return originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        catch (Exception e) {
            return "";
        }
    }
}
