package com.mes.mesBackend.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandling implements ErrorController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpResponse> notFoundException(NotFoundException ex) {
        HttpResponse httpResponse = HttpResponse.of(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(httpResponse, httpResponse.getHttpStatus());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<HttpResponse> badRequestException(BadRequestException ex) {
        HttpResponse httpResponse = HttpResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(httpResponse, httpResponse.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> errorException(MethodArgumentNotValidException ex) {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getBindingResult());
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<HttpResponse> expiredJwtException(ExpiredJwtException ex) {
        HttpResponse httpResponse = HttpResponse.of(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return new ResponseEntity<>(httpResponse, httpResponse.getHttpStatus());
    }
}
