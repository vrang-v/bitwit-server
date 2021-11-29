package com.server.bitwit.module.post.dto;

import com.server.bitwit.domain.Post;
import lombok.Value;

@Value
public class PostViewer {
    
    Long viewerAccountId;
    
    Post post;
    
}
