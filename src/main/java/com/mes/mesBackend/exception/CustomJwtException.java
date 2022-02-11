package com.mes.mesBackend.exception;

import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.MongoLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomJwtException extends Exception {
    private Logger logger = LoggerFactory.getLogger(CustomJwtException.class);
    private CustomLogger cLogger;

    public CustomJwtException(String message){
        super(message);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.error(message);
    }
}