package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "창고")
public class WareHouseResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "창고코드")
    String wareHouseCode;

    @Schema(description = "창고명")
    String wareHouseName;

    @Schema(description = "창고유형")
    WareHouseTypeResponse wareHouseType;

    @Schema(description = "창고그룹")
    String wareHouseGroup;

    @Schema(description = "사용여부")
    boolean useYn;

    @Schema(description = "공정용 여부")
    boolean workProcessYn;

    @Getter
    @Setter
    public static class idAndName {
        Long id;
        String wareHouseName;   // 창고명
    }
}
