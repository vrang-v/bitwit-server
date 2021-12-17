package com.server.bitwit.module.file;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RequiredArgsConstructor
@RestController
public class FileController {
    
    private final FileStorage fileStorage;
    
    @GetMapping(path = "/images/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource downloadImageFile(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:" +  fileStorage.getFullPath(fileName));
    }
}
