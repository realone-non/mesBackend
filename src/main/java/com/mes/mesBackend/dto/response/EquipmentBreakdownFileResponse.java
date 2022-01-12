package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

// 17-2. 설비고장 수리내역 등록 설비고장 파일
@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "설비 고장 파일")
public class EquipmentBreakdownFileResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "파일 url")
    String fileUrl;
}
