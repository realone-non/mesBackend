package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "개발 품목 등록")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DevelopmentResponse {
    @Schema(description = "개발 품목 ID")
    Long id;

    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "사업명")
    String businessName;

    @Schema(description = "시작일")
    LocalDate startDate;

    @Schema(description = "종료일")
    LocalDate endDate;

    @Schema(description = "납품수량")
    int deliverAmount;

    @Schema(description = "진행상태")
    String processState;

    @Schema(description = "담당자 ID")
    Long userId;

    @Schema(description = "담당자 이름")
    String userName;

    @Schema(description = "파일 갯수")
    int fileCount;
}
