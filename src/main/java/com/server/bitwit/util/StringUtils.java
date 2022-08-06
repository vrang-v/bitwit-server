package com.server.bitwit.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {
    
    public static String getLastElement(String string, String delimiter) {
        String[] arr = string.split(delimiter);
        return arr[arr.length - 1];
    }
    
}
