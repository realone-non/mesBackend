package com.mes.mesBackend.logger;

import com.mes.mesBackend.entity.UserLog;
import com.mes.mesBackend.repository.UserLogRepository;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class LogSender {

    private String certKey = "$5$API$J0FWWtwLS/CLMLcT9ArNNMsPTmT4m/S6ssxh53kg9g5";
    private final UserLogRepository userLog;

    public void sendLog(String method, String userIp) throws ParseException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://log.smart-factory.kr/apisvc/sendLogData.json";
        Boolean isTimeOver = true;
        UserLog dbUserLog = userLog.findTop1ByUseMethodOrderByCreatedDateDesc(method);
        String trimMethod = method.trim();
        LocalDateTime nowTime = LocalDateTime.now();
        if(dbUserLog == null){
            isTimeOver = dbUserLog.getCreatedDate().plusMinutes(1).isBefore(nowTime);
        }

        if(trimMethod.equals("GET") && isTimeOver){
            // API 전송시 APPLICATION_JSON이 아닌 application/x-www-form-urlencoded로 보내야됨. 객체 형태로 보낼 경우, 해당 폼으로 보내기 안됨.
            // 해당 폼으로 보내야 할 경우는 MultiValueMap을 사용해야 함.
            // https://velog.io/@yeon/RestTemplate-%EC%9C%BC%EB%A1%9C-applicationx-www-form-urlencoded-%ED%83%80%EC%9E%85-%EC%9A%94%EC%B2%AD-%EC%A0%84%EC%86%A1%ED%95%98%EA%B8%B0-ITDA-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("crtfcKey", certKey);
            params.add("logDt", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
            params.add("useSe", method);
            params.add("sysUser", "admin");
            //params.add("conectIp", "125.247.37.167");
            params.add("conectIp", userIp);
            params.add("dataUsgqty", "0");

            String response = restTemplate.postForObject(url, params, String.class);

            JSONParser jsonParser = new JSONParser();

            Object responseJson = jsonParser.parse(response);

            JSONObject jsonObj = (JSONObject) responseJson;
            JSONObject result = (JSONObject) jsonObj.get("result");

            UserLog newUserLog = new UserLog();
            newUserLog.setUserIp(userIp);
            newUserLog.setUseMethod(method);
            newUserLog.setUserId("admin");
            newUserLog.setResultCode((String)result.get("recptnRsltCd"));
            newUserLog.setResultMsg((String)result.get("recptnRslt"));

            userLog.save(newUserLog);
        }
        else if(!trimMethod.equals("GET")){
            // API 전송시 APPLICATION_JSON이 아닌 application/x-www-form-urlencoded로 보내야됨. 객체 형태로 보낼 경우, 해당 폼으로 보내기 안됨.
            // 해당 폼으로 보내야 할 경우는 MultiValueMap을 사용해야 함.
            // https://velog.io/@yeon/RestTemplate-%EC%9C%BC%EB%A1%9C-applicationx-www-form-urlencoded-%ED%83%80%EC%9E%85-%EC%9A%94%EC%B2%AD-%EC%A0%84%EC%86%A1%ED%95%98%EA%B8%B0-ITDA-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("crtfcKey", certKey);
            params.add("logDt", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
            params.add("useSe", method);
            params.add("sysUser", "admin");
            //params.add("conectIp", "125.247.37.167");
            params.add("conectIp", userIp);
            params.add("dataUsgqty", "0");

            String response = restTemplate.postForObject(url, params, String.class);

            JSONParser jsonParser = new JSONParser();

            Object responseJson = jsonParser.parse(response);

            JSONObject jsonObj = (JSONObject) responseJson;
            JSONObject result = (JSONObject) jsonObj.get("result");

            UserLog newUserLog = new UserLog();
            newUserLog.setUserIp(userIp);
            newUserLog.setUseMethod(method);
            newUserLog.setUserId("admin");
            newUserLog.setResultCode((String)result.get("recptnRsltCd"));
            newUserLog.setResultMsg((String)result.get("recptnRslt"));

            userLog.save(newUserLog);
        }
    }

}
