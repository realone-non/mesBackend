package com.mes.mesBackend.logger;

import org.slf4j.Logger;

public class MongoLogger extends CustomLogger {

//    @Autowired
//    private MongoTemplate mongoTemplate;

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
        log.setMessage(msg);
//        mongoTemplate.insert(log, "log");
    }

    @Override
    protected void writeDebug(String msg, String userCode, Long resourceId) {
        Log log = new Log();
        log.setLevel("DEBUG");
        log.setMessage(msg);
//        mongoTemplate.insert(log, "log");
    }

    @Override
    protected void writeInfo(String msg) {
        Log log = new Log();
        log.setLevel("INFO");
        log.setMessage(msg);
//        mongoTemplate.insert(log, "log");
    }

    @Override
    protected void writeWarn(String msg, String userCode, Long resourceId) {
        Log log = new Log();
        log.setLevel("WARN");
        log.setMessage(msg);
//        mongoTemplate.insert(log, "log");
    }

    @Override
    protected void writeError(String msg) {
        Log log = new Log();
        log.setLevel("ERROR");
        log.setMessage(msg);
//        mongoTemplate.insert(log, "log");
    }

    /*
    * CustomLogger 를 상속받고 mongoTemplate 을 사용하는 방식이다.
    * MongoLogger 에서 Context 를 가져오기 위해 setMongoTemplate 를 작성한다.
    * */
    private void setMongoTemplate(String mongoTemplateBeanName) {
        // Request 받지 않고 가져오는 방식
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // mongoContext.xml 을 가져오는 방법
//        HttpSession session = request.getSession();
//        ServletContext context = session.getServletContext();
//        WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(context);

//        this.mongoTemplate = (MongoTemplate) webContext.getBean(mongoTemplateBeanName);
    }
}
