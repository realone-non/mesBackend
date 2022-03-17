package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "사업장")
public class WorkPlaceResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "사업장 코드")
    String workPlaceCode;   // 사업장 코드

    @Schema(description = "사업장명")
    String workPlaceName;   // 사업장명

    @Schema(description = "사업자등록번호")
    String businessRegNo;  // 사업자등록번호

    @Schema(description = "대표자명")
    String ceoName;     // 대표자명

    @Schema(description = "우편번호")
    String postalCode;      // 우편번호

    @Schema(description = "주소")
    String address;    // 주소

    @Schema(description = "상세주소")
    String detailAddress;    // 상세주소

    @Schema(description = "영문주소1")
    String engAddress1;     // 영문주소1

    @Schema(description = "영문주소2")
    String engAddress2;    // 영문주소2

    @Schema(description = "업태")
    String type;  // 업태

    @Schema(description = "업종")
    String item;    // 업종

    @Schema(description = "전화번호")
    String telNumber;  // 전화번호

    @Schema(description = "fax번호")
    String faxNumber;   // fax번호

    @Schema(description = "법인코드")
    String corporateCode;   // 법인코드

    @Schema(description = "사용여부")
    boolean useYn;           // 사용여부

    @Getter
    @Setter
    @Schema(description = "사업장")
    public static class idAndName {
        @Schema(description = "고유아이디")
        Long id;

        @Schema(description = "사업장명")
        String workPlaceName;   // 사업장명
    }
}
