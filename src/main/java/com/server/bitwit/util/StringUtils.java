package com.server.bitwit.util;

public class StringUtils {
    
    public static String getLastElement(String string, String delimiter) {
        String[] arr = string.split(delimiter);
        return arr[arr.length - 1];
    }
    
}
