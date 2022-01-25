package com.mes.mesBackend.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.DevelopmentChildrenStatusType;
import com.mes.mesBackend.entity.enumeration.DevelopmentStatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DevelopmentStateRequest {
    @Schema(description = "진행상태(상위)")
    DevelopmentStatusType developmentStatus;

    @Schema(description = "진행상태(하위) [ORDER : 수주, DEVELOPMENT_REQUEST : 개발의뢰서, VALIDATION_CHECK : 타당성 검토, DEVELOPMENT_PLAN : 개발계획서, DEVELOPMENT_START : 개발착수회의, DESIGN_PLAN : 설계, DESIGN_REVIEW : 디자인 리뷰회의, PRODUCT_VERIFICATION : 제품검증, PROTOTYPE_EVALUATION : 시제품 평가 회의, STANDARD_DRAWING : 규격도면, COMPLETE_REPORT : 완료보고, OTHER_DOCUMENT : 기타문서, MINUTES : 회의록]")
    DevelopmentChildrenStatusType developmentChildrenStatus;

    @Schema(description = "승인날짜")
    LocalDate approveDate;

    @Schema(description = "파일 버전")
    int ver;

    @Schema(description = "사용자 ID")
    Long userId;

    @Schema(description = "변경 내용")
    String changeContents;

    @Schema(description = "회의 구분")
    String meetingType;
}
