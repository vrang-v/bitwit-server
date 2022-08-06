package com.server.bitwit.util;

import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.module.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
public class ImageRestTemplate {
    
    private final String imageUrl;
    
    public Image getImage( ) {
        var imageResponse = new RestTemplate( ).getForEntity(imageUrl, byte[].class);
        var imageFileName = StringUtils.getLastElement(imageUrl, "/");
        var contentType   = imageResponse.getHeaders( ).getContentType( );
        if (contentType == null) {
            throw new BitwitException(ErrorCode.INVALID_REQUEST, "Content-Type이 없습니다.");
        }
        if (! contentType.getType( ).equals("image")) {
            throw new BitwitException(ErrorCode.INVALID_REQUEST, "Not an image: " + contentType);
        }
        var extension    = contentType.getSubtype( );
        var imageContent = imageResponse.getBody( );
        return new Image(imageFileName + "." + extension, imageContent);
    }
    
    public record Image(String name, byte[] content) {
        
        @Override
        public boolean equals(Object o) {
            if (this == o) { return true; }
            if (! (o instanceof Image image)) { return false; }
            if (! Objects.equals(name, image.name)) { return false; }
            return Arrays.equals(content, image.content);
        }
        
        @Override
        public int hashCode( ) {
            int result = name != null ? name.hashCode( ) : 0;
            result = 31 * result + Arrays.hashCode(content);
            return result;
        }
        
        @Override
        public String toString( ) {
            return "Image{" +
                   "name='" + name + '\'' +
                   ", content=" + Arrays.toString(content) +
                   '}';
        }
    }
}
