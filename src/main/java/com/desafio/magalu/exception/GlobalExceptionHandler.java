package com.desafio.magalu.exception;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            EmptyResultDataAccessException.class
    })
    public ResponseEntity errorNotFound(Exception e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({
            ObjectNotFoundException.class
    })
    public ResponseEntity errorNotFound(ObjectNotFoundException ex) {
        ErroDetails erroDetails = new ErroDetails(new Date(), ex.getMessage(), "");
        return new ResponseEntity(erroDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    public ResponseEntity errorClientAlreadyExists(UserEmailAlreadyExistsException ex) {
        ErroDetails erroDetails = new ErroDetails(new Date(), ex.getMessage(), "");
        return new ResponseEntity(erroDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductApiException.class)
    public ResponseEntity errorProductApiException(ProductApiException ex) {
        ErroDetails erroDetails = new ErroDetails(new Date(), ex.getMessage(), "");
        return new ResponseEntity(erroDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserDoesNotMatchAuthorizationException.class)
    public ResponseEntity erroUsersDoesNotMatch(UserDoesNotMatchAuthorizationException ex) {
        ErroDetails erroDetails = new ErroDetails(new Date(), ex.getMessage(), "");
        return new ResponseEntity(erroDetails, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErroDetails erroDetails = new ErroDetails(new Date(), "Validation Erro",
                ex.getBindingResult().getFieldError().getDefaultMessage());

        return new ResponseEntity(erroDetails, HttpStatus.BAD_REQUEST);
    }

}
