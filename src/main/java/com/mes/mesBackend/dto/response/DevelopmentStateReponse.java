package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.DevelopmentChildrenStatusType;
import com.mes.mesBackend.entity.enumeration.DevelopmentStatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
    LocalDate addDate;

    @Schema(description = "등록자ID")
    Long userId;

    @Schema(description = "등록자 이름")
    String userName;

    @Schema(description = "승인일시")
    LocalDate approveDate;

    @Schema(description = "개발 프로세스(상위)")
    DevelopmentStatusType status;

    @Schema(description = "개발 프로세스(하위)")
    DevelopmentChildrenStatusType childrenStatus;

    @Schema(description = "파일 버전")
    int ver;

    @Schema(description = "변경 내용")
    String changeContents;

    @Schema(description = "회의 구분")
    String meetingType;

}
