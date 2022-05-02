package com.server.bitwit.infra.storage.cloud.gcp;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.server.bitwit.domain.UploadFile;
import com.server.bitwit.infra.storage.StorageService;
import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.module.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.storage.GoogleStorageResource;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Profile("!local")
@Service
public class GoogleCloudStorageService implements StorageService {
    
    @Value("${gcp.storage.bucket-name}")
    private String bucketName;
    
    private final Storage storage;
    
    @Override
    public UploadFile upload(String fileName, byte[] content) {
        try {
            var uploadFileName   = generateUploadFileName(fileName);
            new GoogleStorageResource(storage, getLocationUri(uploadFileName))
                    .createBlob(content);
            return new UploadFile(fileName, uploadFileName);
        }
        catch (NullPointerException e) {
            throw new BitwitException("Failed to store unnamed file " + fileName, e);
        }
        catch (StorageException e) {
            throw new BitwitException(e.getMessage( ), e);
        }
    }
    
    @Override
    public Resource download(String filename) {
        return new GoogleStorageResource(storage, getLocationUri(filename));
    }
    
    public void delete(String filename) {
        try {
            var deleted = storage.delete(BlobId.of(bucketName, filename));
            if (! deleted) {
                throw new BitwitException(ErrorCode.FILE_NOT_FOUND, filename + "에 해당하는 파일을 찾을 수 없습니다.");
            }
        }
        catch (StorageException e) {
            throw new BitwitException(e);
        }
    }
    
    private String getLocationUri(String filename) {
        return String.format("gs://%s/%s", bucketName, filename);
    }
}
