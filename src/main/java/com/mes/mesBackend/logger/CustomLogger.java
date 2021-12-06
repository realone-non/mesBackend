package com.mes.mesBackend.logger;

import org.slf4j.Logger;


public abstract class CustomLogger {

    protected Logger logger;

    public CustomLogger(Logger logger) {
        this.logger = logger;
    }

    public void info(String msg) {
        logger.info(msg);
        writeInfo(msg);
    }

    public void error(String msg) {
        logger.error(msg);
        writeError(msg);
    }

    public void trace(String msg, String userCode, Long resourceId) {
        logger.trace(msg);
        writeTrace(msg, userCode, resourceId);
    }

    public void debug(String msg, String userCode, Long resourceId) {
        logger.debug(msg);
        writeDebug(msg, userCode, resourceId);
    }

    public void warn(String msg, String userCode, Long resourceId) {
        logger.warn(msg);
        writeWarn(msg, userCode, resourceId);
    }

    protected abstract void writeError(String msg);
    protected abstract void writeInfo(String msg);

    protected abstract void writeTrace(String msg, String userCode, Long resourceId);
    protected abstract void writeDebug(String msg, String userCode, Long resourceId);
    protected abstract void writeWarn(String msg, String userCode, Long resourceId);
}
