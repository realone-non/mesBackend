package com.mes.mesBackend.exception;

import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.MongoLogger;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;

@RequiredArgsConstructor
public class BadRequestException extends Exception {
    private final Logger logger = LoggerFactory.getLogger(BadRequestException.class);
    private CustomLogger cLogger;

    public BadRequestException(String message) {
        super(message);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.error(message);
    }
}
