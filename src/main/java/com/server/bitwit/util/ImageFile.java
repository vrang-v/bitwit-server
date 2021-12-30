package com.server.bitwit.util;

import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.module.error.exception.ErrorCode;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class ImageFile implements MultipartFile {
    
    private final byte[] content;
    private final String filename;
    private final String contentType;
    
    public ImageFile(byte[] content, String filename, String contentType) {
        String[] split = contentType.split("/");
        if (! split[0].equals("image")) {
            throw new BitwitException(ErrorCode.FIELD_ERROR, "Not an image: " + filename);
        }
        if (! filename.contains(".")) {
            filename += "." + split[1];
        }
        
        this.content     = content;
        this.filename    = filename;
        this.contentType = contentType;
    }
    
    @Override
    public String getName( ) {
        return "image";
    }
    
    @Override
    public String getOriginalFilename( ) {
        return this.filename;
    }
    
    @Override
    public String getContentType( ) {
        return contentType;
    }
    
    @Override
    public boolean isEmpty( ) {
        return content == null || content.length == 0;
    }
    
    @Override
    public long getSize( ) {
        return content.length;
    }
    
    @Override
    public byte[] getBytes( ) throws IOException {
        return content;
    }
    
    @Override
    public InputStream getInputStream( ) throws IOException {
        return new ByteArrayInputStream(content);
    }
    
    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(content);
    }
}