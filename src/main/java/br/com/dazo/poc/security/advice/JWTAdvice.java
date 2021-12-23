package br.com.dazo.poc.security.advice;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class JWTAdvice {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ErrorResponse handleException(AccessDeniedException exception) {
        return new ErrorResponse("teste", exception.getMessage());
    }
}
