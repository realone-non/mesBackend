package com.mes.mesBackend.exception;

public class ExpiredJwtException extends Exception {
    public ExpiredJwtException(String message) {
        super(message);
    }
}
