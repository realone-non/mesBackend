package com.mes.mesBackend.logger;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

public class MongoLogger extends CustomLogger {

    @Autowired
    private MongoTemplate mongoTemplate;

    private MongoLogger(Logger logger) {
        super(logger);
    }

    public MongoLogger(Logger logger, MongoTemplate mongoTemplate) {
        super(logger);
    }

//    public MongoLogger(Logger logger, String mongoTemplateBeanName) {
//        super(logger);
//    }

    public MongoLogger(Logger logger, String mongoTemplateBeanName) {
        super(logger);
        setMongoTemplate(mongoTemplateBeanName);
    }


    // 1. Level : Trace, Debug, Info, Warn, Error
    // 2. 시간: 언제
    // 3. 요청자: 누가
    // 4. 로그메세지: 무엇

    @Override
    public void writeTrace(String msg, String userCode, Long resourceId) {
        Log log = new Log();
        log.setLevel("TRACE");
        log.setRequester(logger.getName());
        log.setUserCode(userCode);
        log.setResourceId(resourceId);
        log.setMessage(msg);
        mongoTemplate.insert(log, "log");
    }

    @Override
    protected void writeDebug(String msg, String userCode, Long resourceId) {
        Log log = new Log();
        log.setLevel("DEBUG");
//        log.setrequester(LocalDateTime.now());
        log.setRequester(logger.getName());
        log.setMessage(msg);
        mongoTemplate.insert(log, "log");
    }

    @Override
    protected void writeInfo(String msg, String userCode, Long resourceId) {
        Log log = new Log();
        log.setLevel("INFO");
//        log.setDateTime(LocalDateTime.now());
        log.setRequester(logger.getName());
        log.setMessage(msg);
        mongoTemplate.insert(log, "log");
    }

    @Override
    protected void writeWarn(String msg, String userCode, Long resourceId) {
        Log log = new Log();
        log.setLevel("WARN");
//        log.setDateTime(LocalDateTime.now());
        log.setRequester(logger.getName());
        log.setMessage(msg);
        mongoTemplate.insert(log, "log");
    }

    @Override
    protected void writeError(String msg, String userCode, Long resourceId) {
        Log log = new Log();
        log.setLevel("ERROR");
//        log.setDateTime(LocalDateTime.now());
        log.setRequester(logger.getName());
        log.setMessage(msg);
        mongoTemplate.insert(log, "log");
    }

    /*
    * CustomLogger 를 상속받고 mongoTemplate 을 사용하는 방식이다.
    * MongoLogger 에서 Context 를 가져오기 위해 setMongoTemplate 를 작성한다.
    * */
    private void setMongoTemplate(String mongoTemplateBeanName) {
        // Request 받지 않고 가져오는 방식
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // mongoContext.xml 을 가져오는 방법
        HttpSession session = request.getSession();
        ServletContext context = session.getServletContext();
        WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(context);

        this.mongoTemplate = (MongoTemplate) webContext.getBean(mongoTemplateBeanName);
    }
}
