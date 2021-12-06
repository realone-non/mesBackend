package com.mes.mesBackend.logger;

import org.slf4j.Logger;


public abstract class CustomLogger {
    /*
    * 추상클래스로 선언
    * Logger를 protected는 상속받은 클래스에서 사용을 가능하게 하기 위해서임.
    * 그리고 CustomLogger를 생성한 위치에 MongoLogger를 생성한다.
    * */

    // 상속받은 자식, 같은 패키지 안에서만 사용 가능.
    protected Logger logger;

    // 기본생성자
    public CustomLogger(Logger logger) {
        this.logger = logger;
    }

    public void trace(String msg, String userCode, Long resourceId) {
        logger.trace(msg);
        writeTrace(msg, userCode, resourceId);
    }

    public void debug(String msg, String userCode, Long resourceId) {
        logger.debug(msg);
        writeDebug(msg, userCode, resourceId);
    }

    public void createInfo(String userCode, Long resourceId, String controllerName) {
        logger.info(userCode + " is created the " + resourceId + " from " + controllerName);
        createWriteInfo(userCode, resourceId, controllerName);
    }
    public void getInfo(String userCode, Long resourceId, String controllerName) {
        logger.info("INFO");
        createWriteInfo(userCode, resourceId, controllerName);
    }
    public void getListInfo(String userCode, Long resourceId, String controllerName) {
        logger.info("INFO");
        createWriteInfo(userCode, resourceId, controllerName);
    }
    public void updateInfo(String userCode, Long resourceId, String controllerName) {
        logger.info("INFO");
        createWriteInfo(userCode, resourceId, controllerName);
    }
    public void deleteInfo(String userCode, Long resourceId, String controllerName) {
        logger.info("INFO");
        createWriteInfo(userCode, resourceId, controllerName);
    }

    public void warn(String msg, String userCode, Long resourceId) {
        logger.warn(msg);
        writeWarn(msg, userCode, resourceId);
    }

    public void error(String msg, String userCode, Long resourceId) {
        logger.error(msg);
        writeError(msg, userCode, resourceId);
    }

    protected abstract void writeTrace(String msg, String userCode, Long resourceId);
    protected abstract void writeDebug(String msg, String userCode, Long resourceId);

    protected abstract void createWriteInfo(String userCode, Long resourceId, String controllerName);
    protected abstract void getWriteInfo(String userCode, Long resourceId, String controllerName);
    protected abstract void getListWriteInfo(String userCode, Long resourceId, String controllerName);
    protected abstract void updateWriteInfo(String userCode, Long resourceId, String controllerName);
    protected abstract void deleteWriteInfo(String userCode, Long resourceId, String controllerName);

    protected abstract void writeWarn(String msg, String userCode, Long resourceId);
    protected abstract void writeError(String msg, String userCode, Long resourceId);
}
