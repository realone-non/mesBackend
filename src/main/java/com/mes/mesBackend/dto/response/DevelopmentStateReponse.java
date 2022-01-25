package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.DevelopmentChildrenStatusType;
import com.mes.mesBackend.entity.enumeration.DevelopmentStatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "개발 품목 등록 상세")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DevelopmentStateReponse {
    @Schema(description = "개발 품목 상세 ID")
    Long id;

    @Schema(description = "진행상태 파일")
    String fileUrl;

    @Schema(description = "등록일자")
    LocalDateTime addDate;

    @Schema(description = "등록자ID")
    Long userId;

    @Schema(description = "등록자 이름")
    String userName;

    @Schema(description = "승인일시")
    LocalDate approveDate;

    @Schema(description = "개발 프로세스(상위)")
    DevelopmentStatusType status;

    @Schema(description = "[개발 프로세스(하위)] ORDER: 수주, DEVELOPMENT_REQUEST: 개발의뢰서, VALIDATION_CHECK : 타당성 검토, DEVELOPMENT_PLAN : 개발계획서, DEVELOPMENT_START : 개발착수회의, DESIGN_PLAN : 설계, DESIGN_REVIEW : 디자인 리뷰회의, PRODUCT_VERIFICATION : 제품검증, PROTOTYPE_EVALUATION : 시제품 평가 회의, STANDARD_DRAWING : 규격도면, COMPLETE_REPORT : 완료보고, OTHER_DOCUMENT : 기타문서, MINUTES : 회의록")
    DevelopmentChildrenStatusType childrenStatus;

    @Schema(description = "파일 버전")
    int ver;

    @Schema(description = "변경 내용")
    String changeContents;

    @Schema(description = "회의 구분")
    String meetingType;

}
