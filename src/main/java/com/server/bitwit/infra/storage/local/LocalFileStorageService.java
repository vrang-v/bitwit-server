package com.server.bitwit.infra.storage.local;

import com.server.bitwit.domain.UploadFile;
import com.server.bitwit.infra.storage.StorageService;
import com.server.bitwit.module.error.exception.BitwitException;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;

@Profile("local")
@Service
public class LocalFileStorageService implements StorageService {
    
    @SneakyThrows
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
        
        file.transferTo(getAbsolutePath(uploadFileName));
        
        return new UploadFile(originalFilename, uploadFileName);
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
        return Path.of("C:\\Users\\pc\\Desktop\\bitwit\\bitwit-server\\image\\", uploadFileName);
    }
}
