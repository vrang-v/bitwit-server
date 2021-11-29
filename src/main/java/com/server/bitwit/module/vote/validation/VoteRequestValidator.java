package com.server.bitwit.module.vote.validation;

import com.server.bitwit.module.error.exception.FieldErrorException;
import com.server.bitwit.module.stock.StockService;
import com.server.bitwit.module.vote.dto.CreateVoteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class VoteRequestValidator implements Validator {
    
    private final StockService stockService;
    
    @Override
    public boolean supports(Class<?> clazz) {
        return CreateVoteRequest.class.isAssignableFrom(clazz);
    }
    
    @Override
    public void validate(Object target, Errors errors) {
        var request = (CreateVoteRequest)target;
        
        var startTime = request.getStartAt( );
        var endTime   = request.getEndedAt( );
        
        if (startTime.isAfter(endTime)) {
            errors.rejectValue("startAt", "time", "시작 시간을 끝나는 시간 이전으로 설정해주세요.");
            errors.rejectValue("endedAt", "time", "끝나는 시간을 시작 시간 이후로 설정해주세요.");
        }
        
        if (! stockService.existById(request.getStockId( ))) {
            errors.rejectValue("stockId", "invalidId", "유효하지 않은 stockId 입니다.");
        }
        
        if (errors.hasErrors( )) {
            throw new FieldErrorException(errors.getAllErrors( ));
        }
    }
}
