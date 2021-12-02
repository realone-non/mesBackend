package com.mes.mesBackend.exception;

public class MalformedJwtException extends Exception {
    public MalformedJwtException(String message) {
        super(message);
    }
}
