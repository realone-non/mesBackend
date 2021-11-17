package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDateTime startDate;

    @Schema(description = "유효종료일")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDateTime endDate;

    @Schema(description = "개발상태")
    String developStatus;

    @Schema(description = "비고")
    String note;

    @Schema(description = "승인일시")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDateTime approvalDate;

    @Schema(description = "사용")
    Boolean useYn;
}
