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

    @Schema(description = "진행상태(하위)")
    DevelopmentChildrenStatusType developmentChildrenStatus;

    @Schema(description = "파일 URL")
    String fileUrl;

    @Schema(description = "등록일자")
    LocalDate createdDate;

    @Schema(description = "승인날짜")
    LocalDate approveDate;

    @Schema(description = "파일 버전")
    int ver;

    @Schema(description = "변경 내용")
    String changeContents;

    @Schema(description = "회의 구분")
    String meetingType;
}
