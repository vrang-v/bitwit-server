package com.server.bitwit.util;

import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

public class ResultMatcherUtils {
    
    public static ResultMatcher matchAllItem(List<ResultMatcher> matchers) {
        return result -> {
            for (var matcher : matchers) {
                matcher.match(result);
            }
        };
    }
    
}
