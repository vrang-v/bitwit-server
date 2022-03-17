package com.server.bitwit.module.file;

import com.server.bitwit.infra.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FileController {
    
    private final StorageService storageService;
    
    @GetMapping(path = "/images/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource downloadImageFile(@PathVariable String fileName) {
        return storageService.download(fileName);
    }
}
