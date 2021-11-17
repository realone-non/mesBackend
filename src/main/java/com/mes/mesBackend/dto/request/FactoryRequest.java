package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "공장")
public class FactoryRequest {

    @Schema(description = "공장코드")
    @NotBlank(message = NOT_NULL)
    String factoryCode;     // 공장코드

    @Schema(description = "공장명")
    @NotBlank(message = NOT_NULL)
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
    @NotBlank(message = NOT_NULL)
    String lotCode;         // LOT용 코드

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn;           // 사용

    @Schema(description = "사업장 id")
    @NotNull(message = NOT_NULL)
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    Long workPlaceId;       // 사업장
}
