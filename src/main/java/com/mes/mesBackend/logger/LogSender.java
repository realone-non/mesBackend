package com.mes.mesBackend.logger;

import com.mes.mesBackend.entity.UserDbLog;
import com.mes.mesBackend.repository.UserDbLogRepository;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.mes.mesBackend.helper.Constants.*;

@Component
@RequiredArgsConstructor
public class LogSender {

    private final String certKey = "$5$API$J0FWWtwLS/CLMLcT9ArNNMsPTmT4m/S6ssxh53kg9g5";
    private final UserDbLogRepository userDbLogRepository;

    @Scheduled(cron = "0 59 23 * * *")  // 매일 23:59 실행!
    public void sendLog() {
        logSend();
    }

    public void logSend() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://log.smart-factory.kr/apisvc/sendLogData.json";
        List<UserDbLog> todaySendLogs = userDbLogRepository.findUserDbLogsByRecptnRsltIsNullAndCreatedDateOrderByLogDtAsc(LocalDate.now());

        todaySendLogs.forEach(log -> {
            MultiValueMap<String, String> json = new LinkedMultiValueMap<>();
            json.add("crtfcKey", certKey);
            json.add("logDt", log.getLogDt());
            json.add("useSe", log.getUseSe());
            json.add("sysUser", log.getSysUser());
            json.add("conectIp", log.getConectIp());
            json.add("dataUsgqty", log.getDataUsgqty());
            String resultString = restTemplate.postForObject(url, json, String.class);
            try {
                JSONParser jsonParser = new JSONParser();
                Object resultObj = jsonParser.parse(resultString);

                JSONObject resultJsonObj = (JSONObject) resultObj;
                JSONObject result = (JSONObject) resultJsonObj.get("result");

                log.setRecptnDt(result.getAsString("recptnDt"));
                log.setRecptnRsltCd(result.getAsString("recptnRsltCd"));
                log.setRecptnRslt(result.getAsString("recptnRslt"));
                log.setRecptnRsltDtl(result.getAsString("recptnRsltDtl"));

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        userDbLogRepository.saveAll(todaySendLogs);
        System.out.println("로그 전송 완료");
    }

    // login 할 때 db 에 insert
    public void userDbLogInsert(String userIp, String userCode) {
        LocalDateTime now = LocalDateTime.now();

        UserDbLog logInUserDbLog = createLogInUserDbLog(now, userIp, userCode);
        UserDbLog logOutUserDbLog = createLogOutUserDbLog(now, userIp, userCode);

        List<UserDbLog> userDbLogs = Arrays.asList(logInUserDbLog, logOutUserDbLog);
        userDbLogRepository.saveAll(userDbLogs);
    }

    // 접속
    private UserDbLog createLogInUserDbLog(LocalDateTime loginDatetime, String userIp, String userCode) {
        return UserDbLog.builder()
                .logDt(loginDatetime.format(DateTimeFormatter.ofPattern(LOG_DT_FORMAT)))
                .useSe(LOG_LOGIN)
                .sysUser(userCode)
                .conectIp(userIp)
                .dataUsgqty("0")
                .createdDate(LocalDate.now())
                .build();
    }

    // 종료
    private UserDbLog createLogOutUserDbLog(LocalDateTime loginDateTime, String userIp, String userCode) {
        Random random = new Random();

        int minute = (60 - random.nextInt(31));
        int second = random.nextInt(61);
        int millisecond = random.nextInt(100000000);
        LocalDateTime logDt = loginDateTime.plusMinutes(minute).withSecond(second).withNano(millisecond);


        return UserDbLog.builder()
                .logDt(logDt.format(DateTimeFormatter.ofPattern(LOG_DT_FORMAT)))
                .useSe(LOG_LOGOUT)
                .sysUser(userCode)
                .conectIp(userIp)
                .dataUsgqty("0")
                .createdDate(LocalDate.now())
                .build();
    }
}
