package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.DevelopStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mes.mesBackend.helper.Constants.ASIA_SEOUL;
import static com.mes.mesBackend.helper.Constants.YYYY_MM_DD;


@Getter
@Setter
@Schema(description = "BOM 마스터")
@JsonInclude(NON_NULL)
public class BomMasterResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "품목 정보")
    ItemResponse.itemToBomResponse item;

    @Schema(description = "BOM 번호")
    int bomNo;

    @Schema(description = "유효시작일")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    LocalDateTime startDate;

    @Schema(description = "유효종료일")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    LocalDateTime endDate;

    @Schema(description = "개발상태")
    DevelopStatus developStatus;

    @Schema(description = "비고")
    String note;

    @Schema(description = "승인일시")
    @JsonFormat(pattern = YYYY_MM_DD, timezone = ASIA_SEOUL)
    LocalDateTime approvalDate;

    @Schema(description = "사용")
    Boolean useYn;
}
