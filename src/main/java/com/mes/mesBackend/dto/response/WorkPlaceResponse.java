package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class WorkPlaceResponse {
    Long id;
    String workPlaceCode;   // 사업장 코드
    String workPlaceName;   // 사업장명
    String businessRegNo;  // 사업자등록번호
    String ceoName;     // 대표자명
    String postalCode;      // 우편번호
    String address;    // 주소
    String detailAddress;    // 상세주소
    String engAddress1;     // 영문주소1
    String engAddress2;    // 영문주소2
    List<BusinessTypeResponse> type;  // 업태
    String item;    // 업종
    String telNumber;  // 전화번호
    String faxNumber;   // fax번호
    String corporateCode;   // 법인코드
    boolean useYn;           // 사용여부
}
