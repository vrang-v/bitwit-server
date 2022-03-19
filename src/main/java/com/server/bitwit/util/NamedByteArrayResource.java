package com.server.bitwit.util;

import org.springframework.core.io.ByteArrayResource;

import java.util.Objects;

public class NamedByteArrayResource extends ByteArrayResource {
    
    private final String fileName;
    
    public NamedByteArrayResource(String fileName, byte[] byteArray) {
        super(byteArray);
        this.fileName = fileName;
    }
    
    public NamedByteArrayResource(String fileName, byte[] byteArray, String description) {
        super(byteArray, description);
        this.fileName = fileName;
    }
    
    @Override
    public String getFilename( ) {
        return this.fileName;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (! (o instanceof NamedByteArrayResource)) { return false; }
        if (! super.equals(o)) { return false; }
        NamedByteArrayResource that = (NamedByteArrayResource)o;
        return Objects.equals(fileName, that.fileName);
    }
    
    @Override
    public int hashCode( ) {
        return Objects.hash(super.hashCode( ), fileName);
    }
}
