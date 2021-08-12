package com.server.bitwit.module.security.jwt.support;

import com.server.bitwit.module.security.jwt.Jwt;
import com.server.bitwit.module.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Component
public class JwtArgumentResolver implements HandlerMethodArgumentResolver {
    
    private final JwtService jwtService;
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Jwt.class)
               && parameter.getParameterType( ).equals(parameter.getParameterAnnotation(Jwt.class).claim( ).getType( ));
    }
    
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        var httpRequest = (HttpServletRequest)webRequest.getNativeRequest( );
        var jwt         = jwtService.extractJwt(httpRequest);
        var claim       = parameter.getParameterAnnotation(Jwt.class).claim( );
        return jwtService.extractClaim(jwt, claim);
    }
}
