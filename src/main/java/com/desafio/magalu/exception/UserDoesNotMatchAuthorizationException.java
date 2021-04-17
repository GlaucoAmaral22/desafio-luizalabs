package com.desafio.magalu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserDoesNotMatchAuthorizationException extends RuntimeException {

    public UserDoesNotMatchAuthorizationException(String message) {
        super(message);
    }

    public UserDoesNotMatchAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

}
