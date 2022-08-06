package com.server.bitwit.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static org.springframework.util.StringUtils.hasText;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FormatUtils {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z\\d._%+-]+@[A-Z\\d.-]+\\.[A-Z]{2,6}$", CASE_INSENSITIVE
    );
    
    public static boolean isEmailFormat(String s) {
        return hasText(s) && EMAIL_PATTERN.matcher(s).matches( );
    }
}
