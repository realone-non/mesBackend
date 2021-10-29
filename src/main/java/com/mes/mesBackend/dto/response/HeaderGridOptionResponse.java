package com.mes.mesBackend.dto.response;

public class HeaderGridOptionResponse {
    Long id;
    String header;              // 헤더
    String controllerName;      // 컨트롤러 이름
    String columnName;          // 컬럼명
    int seq;                    // 순서
    GridOptionResponse gridOptionResponse;      // 그리드 옵션
}
