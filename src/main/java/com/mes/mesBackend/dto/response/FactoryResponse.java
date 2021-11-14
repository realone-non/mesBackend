package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "공장")
public class FactoryResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "공장코드")
    String factoryCode;     // 공장코드

    @Schema(description = "공장명")
    String factoryName;     // 공장명

    @Schema(description = "약어")
    String shortName;       // 약어

    @Schema(description = "우편번호")
    String postalCode;      // 우편번호

    @Schema(description = "기본주소")
    String address;         // 기본주소

    @Schema(description = "상세주소")
    String detailAddress;   // 상세주소

    @Schema(description = "영문주소1")
    String engAddress1;     // 영문주소1

    @Schema(description = "영문주소2")
    String engAddress2;     // 영문주소2

    @Schema(description = "전화번호")
    String telNumber;       // 전화번호

    @Schema(description = "fax번호")
    String faxNumber;       // fax번호

    @Schema(description = "LOT용 코드")
    String lotCode;         // LOT용 코드

    @Schema(description = "사용여부")
    boolean useYn;           // 사용

    @Schema(description = "사업장")
    String workPlaceName;   // 사업장
}
