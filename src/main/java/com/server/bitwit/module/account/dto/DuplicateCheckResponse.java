package com.server.bitwit.module.account.dto;

import com.server.bitwit.module.common.dto.BitwitResponse;
import lombok.Value;

@Value
public class DuplicateCheckResponse implements BitwitResponse
{
    boolean result;
}
