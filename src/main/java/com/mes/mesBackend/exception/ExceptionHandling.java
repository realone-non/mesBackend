package com.mes.mesBackend.exception;

import com.mes.mesBackend.entity.HttpResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandling implements ErrorController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpResponse> notFoundException(NotFoundException notFoundException) {
        return createHttpResponse(HttpStatus.NOT_FOUND, notFoundException.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<HttpResponse> badRequestException(BadRequestException badRequestException) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, badRequestException.getMessage());
    }

    // createHttpResponse
    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message), httpStatus);
    }
}
