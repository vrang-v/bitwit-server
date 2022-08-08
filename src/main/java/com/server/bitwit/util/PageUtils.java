package com.server.bitwit.util;

import org.springframework.data.domain.Sort;

public interface PageUtils {
    
    static Sort getSort(String sort) {
        if (sort == null) {
            return null;
        }
        var split = sort.split(",");
        if (split.length != 2) {
            throw new IllegalArgumentException("Invalid sort parameter : " + sort);
        }
        return switch (split[1]) {
            case "asc" -> Sort.by(Sort.Direction.ASC, split[0]);
            case "desc" -> Sort.by(Sort.Direction.DESC, split[0]);
            default -> throw new IllegalArgumentException("Invalid sort parameter : " + sort);
        };
    }
    
}
