package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.mes.mesBackend.exception.Message.NOT_EMPTY;
import static com.mes.mesBackend.exception.Message.NOT_NULL;

@Getter
@Setter
@Schema(description = "사업장")
public class WorkPlaceRequest {
    @Schema(description = "사업장 코드")
    @NotBlank(message = NOT_EMPTY)
    String workPlaceCode;   // 사업장 코드

    @Schema(description = "사업장명")
    @NotBlank(message = NOT_EMPTY)
    String workPlaceName;   // 사업장명

    @Schema(description = "사업자등록번호")
    String businessRegNo;  // 사업자등록번호

    @Schema(description = "대표자명")
    @NotBlank(message = NOT_EMPTY)
    String ceoName;     // 대표자명

    @Schema(description = "우편번호")
    @NotBlank(message = NOT_EMPTY)
    String postalCode;      // 우편번호

    @Schema(description = "주소")
    @NotBlank(message = NOT_EMPTY)
    String address;    // 주소

    @Schema(description = "상세주소")
    String detailAddress;    // 상세주소

    @Schema(description = "영문주소1")
    String engAddress1;     // 영문주소1

    @Schema(description = "영문주소2")
    String engAddress2;    // 영문주소2

    @Schema(description = "업태 ids")
    List<Long> type;  // 업태

    @Schema(description = "업종")
    String item;    // 업종

    @Schema(description = "전화번호")
    @NotBlank(message = NOT_EMPTY)
    String telNumber;  // 전화번호

    @Schema(description = "fax번호")
    String faxNumber;   // fax번호

    @Schema(description = "법인코드")
    String corporateCode;   // 법인코드

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn;           // 사용여부
}
