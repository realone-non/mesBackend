package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "개발품목 등록")
public class DevelopmentRequest {
    @Schema(description = "품번")
    String itemNo;

    @Schema(description = "품명")
    String itemName;

    @Schema(description = "사업명")
    String businessName;

    @Schema(description = "등록자 ID")
    Long userId;

    @Schema(description = "시작일")
    LocalDate startDate;

    @Schema(description = "종료일")
    LocalDate endDate;

    @Schema(description = "납품수량")
    int deliverAmount;
}
