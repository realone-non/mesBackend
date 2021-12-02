package com.mes.mesBackend.exception;

public class CustomJwtException extends Exception {
    public CustomJwtException(String message){
        super(message);
    }
}