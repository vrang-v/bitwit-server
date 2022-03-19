package com.server.bitwit.module.file;

import org.springframework.core.io.AbstractResource;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class MultipartFileResource extends AbstractResource {
    
    private final MultipartFile multipartFile;
    
    public MultipartFileResource(MultipartFile multipartFile) {
        Assert.notNull(multipartFile, "MultipartFile must not be null");
        this.multipartFile = multipartFile;
    }
    
    /**
     * This implementation always returns {@code true}.
     */
    @Override
    public boolean exists( ) {
        return true;
    }
    
    /**
     * This implementation always returns {@code true}.
     */
    @Override
    public boolean isOpen( ) {
        return true;
    }
    
    @Override
    public long contentLength( ) {
        return this.multipartFile.getSize( );
    }
    
    @Override
    public String getFilename( ) {
        return this.multipartFile.getOriginalFilename( );
    }
    
    /**
     * This implementation throws IllegalStateException if attempting to
     * read the underlying stream multiple times.
     */
    @Override
    public InputStream getInputStream( ) throws IOException, IllegalStateException {
        return this.multipartFile.getInputStream( );
    }
    
    /**
     * This implementation returns a description that has the Multipart name.
     */
    @Override
    public String getDescription( ) {
        return "MultipartFile resource [" + this.multipartFile.getName( ) + "]";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (! (o instanceof MultipartFileResource)) { return false; }
        if (! super.equals(o)) { return false; }
        MultipartFileResource that = (MultipartFileResource)o;
        return Objects.equals(multipartFile, that.multipartFile);
    }
    
    @Override
    public int hashCode( ) {
        return Objects.hash(super.hashCode( ), multipartFile);
    }
}
