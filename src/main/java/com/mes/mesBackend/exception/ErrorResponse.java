package com.mes.mesBackend.exception;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class ErrorResponse {
    private Date timeStamp;
    private String message;
    private int httpStatusCode;
    private HttpStatus httpStatus;
    private List<FieldError> errors;

    private ErrorResponse(String message, int httpStatusCode, HttpStatus httpStatus, final List<FieldError> errors) {
        this.timeStamp = new Date();
        this.message = message;
        this.httpStatusCode = httpStatusCode;
        this.httpStatus = httpStatus;
        this.errors = errors;
    }

    public static ErrorResponse of(HttpStatus httpStatus, final BindingResult bindingResult) {
        return new ErrorResponse("INVALID INPUT VALUE", httpStatus.value(), httpStatus, FieldError.of(bindingResult));
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getField() + " " + error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }

}
