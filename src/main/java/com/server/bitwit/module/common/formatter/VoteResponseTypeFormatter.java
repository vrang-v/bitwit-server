package com.server.bitwit.module.common.formatter;

import com.server.bitwit.module.vote.dto.VoteResponseType;
import org.springframework.format.Formatter;

import java.util.Locale;

public class VoteResponseTypeFormatter implements Formatter<VoteResponseType> {
    
    @Override
    public VoteResponseType parse(String text, Locale locale) {
        return VoteResponseType.findByTypeName(text);
    }
    
    @Override
    public String print(VoteResponseType responseType, Locale locale) {
        return responseType.getTypeName( );
    }
}
