package com.server.bitwit.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class PageUtils {
    
    public static Sort getSort(String sort) {
        if (sort == null) {
            throw new NullPointerException("sort parameter is null");
        }
        var split = sort.split(",");
        if (split.length != 2) {
            throw new IllegalArgumentException("Invalid sort parameter : " + sort);
        }
        switch (split[1]) {
            case "asc":
                return Sort.by(Sort.Direction.ASC, split[0]);
            case "desc":
                return Sort.by(Sort.Direction.DESC, split[0]);
            default:
                throw new IllegalArgumentException("Invalid sort parameter : " + sort);
        }
    }
    
}
