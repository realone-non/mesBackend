package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FactoryRequest {
    String factoryCode;     // 공장코드
    String factoryName;     // 공장명
    String shortName;       // 약어
    String postalCode;      // 우편번호
    String address;         // 기본주소
    String detailAddress;   // 상세주소
    String engAddress1;     // 영문주소1
    String engAddress2;     // 영문주소2
    String telNumber;       // 전화번호
    String faxNumber;       // fax번호
    String lotCode;         // LOT용 코드
    boolean useYn;           // 사용
    Long workPlaceId;       // 사업장
}