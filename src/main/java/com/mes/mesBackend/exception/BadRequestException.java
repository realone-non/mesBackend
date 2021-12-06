package com.mes.mesBackend.exception;

import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.MongoLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BadRequestException extends Exception {
    private Logger logger = LoggerFactory.getLogger(BadRequestException.class);
    private CustomLogger cLogger;

    public BadRequestException(String message) {
        super(message);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.error(message);
    }
}
