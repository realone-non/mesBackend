package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "헤더용 창고 목록")
@JsonInclude(NON_NULL)
public class HeaderWarehouseResponse {
    @Schema(description = "창고 ID")
    Long id;

    @Schema(description = "창고 이름")
    String wareHouseName;
}
