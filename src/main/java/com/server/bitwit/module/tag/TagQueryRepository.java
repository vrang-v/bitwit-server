package com.server.bitwit.module.tag;

import com.server.bitwit.domain.Tag;

import java.util.List;

public interface TagQueryRepository {
    
    List<Tag> findAllTagByName(List<String> tags);
    
}
