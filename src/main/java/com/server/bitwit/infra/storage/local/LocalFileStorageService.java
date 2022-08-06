package com.server.bitwit.infra.storage.local;

import com.server.bitwit.domain.UploadFile;
import com.server.bitwit.infra.storage.StorageService;
import com.server.bitwit.module.error.exception.BitwitException;
import org.springframework.beans.factory.annotation.Value;
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
    
    private final String storagePath;
    
    public LocalFileStorageService(@Value("${storage.path}") String storagePath) {
        this.storagePath = storagePath;
    }
    
    @Override
    public UploadFile upload(String fileName, byte[] content) {
        try {
            var uploadFileName = generateUploadFileName(fileName);
            Files.write(getAbsolutePath(uploadFileName), content);
            return new UploadFile(fileName, uploadFileName);
        }
        catch (NullPointerException e) {
            throw new BitwitException("Failed to store unnamed file " + fileName);
        }
        catch (IOException e) {
            throw new BitwitException("Failed to store file " + fileName);
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
        return Path.of(storagePath, uploadFileName);
    }
}
