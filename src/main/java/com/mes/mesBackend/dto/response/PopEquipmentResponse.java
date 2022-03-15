package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "pop 설비")
@JsonInclude(NON_NULL)
public class PopEquipmentResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "설비코드")
    String equipmentCode;

    @Schema(description = "설비명")
    String equipmentName;

    @Schema(description = "작업공정 고유아이디")
    Long workProcessId;

    @Schema(description = "작업공정 명")
    String workProcessName;

    @Schema(description = "생산가능 여부 (true: 생산가능, false: 생산불가능)")
    boolean produceYn;
}
