package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "품목그룹")
public class ItemGroupRequest {
    @Schema(description = "상위그룹코드")
    String topGroupCode;        // 상위그룹코드

    @Schema(description = "상위그룹명")
    String topGroupName;        // 상위그룹명

    @Schema(description = "그룹명")
    String groupName;           // 그룹명

    @Schema(description = "기본어명")
    String defaultName;         // 기본어명

    @Schema(description = "순번")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    int orders;              // 순번

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn;
}
