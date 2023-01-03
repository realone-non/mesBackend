package com.mes.mesBackend.interceptor;

import com.mes.mesBackend.helper.ClientIpHelper;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogSender;
import com.mes.mesBackend.logger.LogService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class Interceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(Interceptor.class);
    private CustomLogger customLogger;
    private final ClientIpHelper clientIpHelper;
    private final LogSender logSender;
    private final LogService logService;


    // request 가 들어오고, controller 으로 넘어가기 전에 처리
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 클라이언트 실제 접속 IP 조회
        String clientIP = clientIpHelper.getClientIP(request);
        System.out.println(">> clientIP: " + clientIP);
        System.out.println(">> requestUrl: " + request.getRequestURL());
        String method = request.getMethod();

        // mongo db log
//        customLogger = new MongoLogger(logger, MONGO_TEMPLATE);
//        customLogger.info("clientIP: " + clientIP + ", url: " + request.getRequestURL());
        String header = request.getHeader(AUTHORIZATION);
        String userCode = "";
        if (header != null) {
            userCode = logService.getUserCodeFromHeader(header);
            logSender.sendLog(method, clientIP, userCode);
        }


        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    // controller 에서 요청 처리 후, view 로 rendering 하기 전에 처리
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    // controller 에서 요청 처리 후, view 로 rendering 후에 처리
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
