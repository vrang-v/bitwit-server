package com.server.bitwit.infra.config;

import com.server.bitwit.infra.formatter.VoteResponseTypeFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer
{
    @Override
    public void addFormatters(FormatterRegistry registry)
    {
        registry.addFormatter(new VoteResponseTypeFormatter( ));
    }
}
