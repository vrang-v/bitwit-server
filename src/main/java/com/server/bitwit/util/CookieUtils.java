package com.server.bitwit.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtils {
    
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        var cookies = request.getCookies( );
        
        if (! hasCookie(cookies)) {
            return Optional.empty( );
        }
        
        return Arrays.stream(cookies)
                     .filter(cookie -> cookie.getName( ).equals(name))
                     .findFirst( );
    }
    
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        var cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
    
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        var cookies = request.getCookies( );
        if (hasCookie(cookies)) {
            for (var cookie : cookies) {
                if (cookie.getName( ).equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }
    
    public static String serialize(Object object) {
        return Base64.getUrlEncoder( )
                     .encodeToString(SerializationUtils.serialize(object));
    }
    
    public static <T> T deserialize(Cookie cookie, Class<T> clazz) {
        return clazz.cast(SerializationUtils.deserialize(Base64.getUrlDecoder( ).decode(cookie.getValue( ))));
    }
    
    private static boolean hasCookie(Cookie[] cookies) {
        return cookies != null && cookies.length > 0;
    }
}